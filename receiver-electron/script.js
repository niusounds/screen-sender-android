const dgram = require('dgram');
const server = dgram.createSocket('udp4');
const port = 9999;

/**
 * @type {HTMLImageElement}
 */
const imageElement = document.getElementById('image');

/**
 * @type {HTMLDivElement}
 */
const log = document.getElementById('log');

server.on('error', (err) => {
    console.log(`server error:\n${err.stack}`);
    server.close();
});

// msg is a screen image data from Android.
server.on('message', (msg, rinfo) => {
    let imageData = msg.toString('base64');
    let dataUrl = 'data:image/jpeg;base64,' + imageData;
    imageElement.src = dataUrl;
});

server.on('listening', () => {
    const address = server.address();
    log.innerText = `server listening ${address.address}:${address.port}`;
});

server.bind(port);