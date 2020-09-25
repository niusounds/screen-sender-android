# UDP Screen Sender

Capture screen contents of Android and send it to remote by UDP.

Protocol is very simple. Single screen capture frame is compressed to WebP format and sent by single UDP packet. Each UDP packet contains WebP binary data.

Sender app is `app` module.

Receiver apps are `receiver-electron`(for PC) and `receiver-view`(for Android).
