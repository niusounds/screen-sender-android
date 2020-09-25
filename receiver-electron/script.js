const dgram = require('dgram');
const server = dgram.createSocket('udp4');
const port = 9999;

// Get local IP address and set to text field
const os = require('os');
const nets = os.networkInterfaces();

for (const name of Object.keys(nets)) {
  for (const net of nets[name]) {
    // skip over non-ipv4 and internal (i.e. 127.0.0.1) addresses
    if (net.family === 'IPv4' && !net.internal) {
      document.getElementById('ip_address').value = net.address;
    }
  }
}

/**
 * @type {HTMLImageElement}
 */
const imageElement = document.getElementById('image');

const logContainer = document.getElementById('log-container');

server.on('error', (err) => {
  console.log(`server error:\n${err.stack}`);
  server.close();
});

// msg is a screen image data from Android.
server.on('message', (msg, rinfo) => {
  let imageData = msg.toString('base64');
  let dataUrl = 'data:image/webp;base64,' + imageData;
  imageElement.src = dataUrl;
});

server.on('listening', () => {
  const address = server.address();
  log(`server listening ${address.address}:${address.port}`);
});

server.bind(port);

function log(text) {
  let template = document.getElementById('template-message');
  if (!template) {
    console.log('template not found');
    return;
  }

  /**
   * @type {HTMLElement}
   */
  let cloneNode = template.content.cloneNode(true);
  cloneNode.querySelector('.message').textContent = text;
  logContainer.appendChild(cloneNode);
}