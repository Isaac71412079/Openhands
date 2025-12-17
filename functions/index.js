const { setGlobalOptions } = require("firebase-functions");
const { onSchedule } = require("firebase-functions/v2/pubsub");
const admin = require("firebase-admin");
const logger = require("firebase-functions/logger");

admin.initializeApp();

// Limitar contenedores para control de costos
setGlobalOptions({ maxInstances: 10 });

// Función programada: todos los días a las 9:00 AM (hora de La Paz)
exports.dailySignNotification = onSchedule(
  { schedule: "0 9 * * *", timeZone: "America/La_Paz" },
  async (event) => {
    const message = {
      notification: {
        title: "La Seña del Día",
        body: "¡La seña de hoy es 'GRACIAS'! ☀",
      },
      topic: "daily_sign",
    };

    try {
      await admin.messaging().send(message);
      logger.info("Notificación enviada con éxito!");
    } catch (error) {
      logger.error("Error enviando notificación:", error);
    }
  }
);
