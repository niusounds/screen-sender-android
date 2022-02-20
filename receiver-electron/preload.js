const { contextBridge, ipcRenderer } = require('electron');

contextBridge.exposeInMainWorld('electronAPI', {
    onReceiveDataUrl: (callback) => ipcRenderer.on('onReceiveDataUrl', callback),
    queryIpAddr: () => ipcRenderer.invoke('queryIpAddr'),
})