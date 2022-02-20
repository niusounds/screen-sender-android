const dgram = require('dgram');
const port = 9999;

// Get local IP address and set to text field

function startReceiver(
  {
    /**@type {Function} */
    onReceiveDataUrl,
  }
) {
  const server = dgram.createSocket('udp4');
  console.log('startReceiver');

  server.on('error', (err) => {
    console.log(`server error:\n${err.stack}`);
    server.close();
  });

  // msg is a screen image data from Android.
  server.on('message', (msg, rinfo) => {
    let imageData = msg.toString('base64');
    let dataUrl = 'data:image/webp;base64,' + imageData;
    // imageElement.src = dataUrl;
    onReceiveDataUrl(dataUrl)
  });

  server.on('listening', () => {
    const address = server.address();
    console.log(`server listening ${address.address}:${address.port}`);
  });

  server.bind(port);

  return server;
}

module.exports = startReceiver;