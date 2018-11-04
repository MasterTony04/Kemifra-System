"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();
// // Start writing Fire base Functions
// // https://firebase.google.com/docs/functions/typescript
//
exports.sendFcm = functions.https.onCall((data, context) => {
    admin.messaging().send(data).then(value => {
        console.log('Successfully sent : ', value);
    }).catch(reason => {
        console.log('Error sending : ', reason);
    });
});
//# sourceMappingURL=index.js.map