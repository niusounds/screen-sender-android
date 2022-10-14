# UDP Screen Sender

Capture screen contents of Android and send it to remote by UDP.

Protocol is very simple. Single screen capture frame is compressed to WebP format and sent by single UDP packet. Each UDP packet contains WebP binary data.

Sender app is `app` module.

Receiver apps are `receiver-electron`(for PC) and `receiver-view`(for Android).

## How to use this project

1. Prepare 2 android devices.
2. Install `app` module into one device.
3. Install `receiver` module into another device.
4. Make sure to connecting to same wi-fi network.
5. Launch both apps.
6. Enter IP address of `receiver` device into `app` device's text field.
7. Press `app` device's cast button (Floating action button)
8. Sender device's screen will be mirrored to another device.

If you want to mirror android screen to PC, launch `receiver-electron`.

```
cd receiver-electron
npm install
npm start
```
