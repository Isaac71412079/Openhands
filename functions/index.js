const {onSchedule} = require("firebase-functions/v2/pubsub");
const admin = require("firebase-admin");
const logger = require("firebase-functions/logger");

admin.initializeApp();

// Limitar contenedores para control de costos
const {setGlobalOptions} = require("firebase-functions");
setGlobalOptions({maxInstances: 10});

// =================== NOTIFICACIÓN DE LA MAÑANA ===================
exports.morningSignNotification = onSchedule(
    {schedule: "0 9 * * *", timeZone: "America/La_Paz"},
    async () => {
      const message = {
        notification: {
          title: "Buenos Días 🌞",
          body: "¡La seña de hoy es 'HOLA'!",
        },
        topic: "daily_sign",
      };
      try {
        await admin.messaging().send(message);
        logger.info("Notificación de la mañana enviada!");
      } catch (error) {
        logger.error("Error enviando notificación de la mañana:", error);
      }
    },
);

// =================== NOTIFICACIÓN DE MEDIODÍA ===================
exports.noonSignNotification = onSchedule(
    {schedule: "0 12 * * *", timeZone: "America/La_Paz"},
    async () => {
      const message = {
        notification: {
          title: "Mediodía ☀",
          body: "¡La seña del mediodía es 'GRACIAS'!",
        },
        topic: "daily_sign",
      };
      try {
        await admin.messaging().send(message);
        logger.info("Notificación de mediodía enviada!");
      } catch (error) {
        logger.error("Error enviando notificación de mediodía:", error);
      }
    },
);

// =================== NOTIFICACIÓN DE LA TARDE ===================
exports.afternoonSignNotification = onSchedule(
    {schedule: "0 17 * * *", timeZone: "America/La_Paz"},
    async () => {
      const message = {
        notification: {
          title: "Tarde 🌆",
          body: "¡La seña de la tarde es 'ADIOS'!",
        },
        topic: "daily_sign",
      };
      try {
        await admin.messaging().send(message);
        logger.info("Notificación de la tarde enviada!");
      } catch (error) {
        logger.error("Error enviando notificación de la tarde:", error);
      }
    },
);
