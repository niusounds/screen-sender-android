const { app, BrowserWindow, ipcMain } = require('electron');
const path = require('path');
const os = require('os');

const startReceiver = require('./receiver');

const isDebug = process.argv.includes('-d');

let server = null;

function createWindow() {
  // Create the browser window.
  const win = new BrowserWindow({
    width: 1280,
    height: 720,
    webPreferences: {
      preload: path.join(__dirname, 'preload.js'),
    }
  });

  // renderer.js to main.js API
  ipcMain.handle('queryIpAddr', handleQueryIpAddr);

  // and load the index.html of the app.
  win.loadFile('index.html')

  // Open the DevTools.
  if (isDebug) {
    win.webContents.openDevTools()
  }

  server = startReceiver({
    onReceiveDataUrl: (dataUrl) => win.webContents.send('onReceiveDataUrl', dataUrl),
  })
}

async function handleQueryIpAddr() {
  const nets = os.networkInterfaces();
  for (const name of Object.keys(nets)) {
    for (const net of nets[name]) {
      // skip over non-ipv4 and internal (i.e. 127.0.0.1) addresses
      if (net.family === 'IPv4' && !net.internal) {
        return net.address;
      }
    }
  }
}

// This method will be called when Electron has finished
// initialization and is ready to create browser windows.
// Some APIs can only be used after this event occurs.
app.whenReady().then(createWindow)

// Quit when all windows are closed, except on macOS. There, it's common
// for applications and their menu bar to stay active until the user quits
// explicitly with Cmd + Q.
app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') {
    app.quit()
  }
  server.close();
})

app.on('activate', () => {
  // On macOS it's common to re-create a window in the app when the
  // dock icon is clicked and there are no other windows open.
  if (BrowserWindow.getAllWindows().length === 0) {
    createWindow()
  }
})

// In this file you can include the rest of your app's specific main process
// code. You can also put them in separate files and require them here.
