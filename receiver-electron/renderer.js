/**
 * @type {HTMLImageElement}
 */
const imageElement = document.getElementById('image');

const logContainer = document.getElementById('log-container');

window.electronAPI.onReceiveDataUrl((event, dataUrl) => {
  imageElement.src = dataUrl;
});

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

async function showIpAddr() {
  const ipAddr = await window.electronAPI.queryIpAddr();
  document.getElementById('ip_address').value = ipAddr;
}

showIpAddr();