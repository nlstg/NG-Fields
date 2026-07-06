import packageJson from "../../package.json";

const currentYear = new Date().getFullYear();

export const APP_CONFIG = {
  name: "NG-STARs",
  version: packageJson.version,
  copyright: `© ${currentYear}, NG-STARs.`,
  meta: {
    title: "NG-STARs — Application de gestion terrain",
    description:
      "NG-STARs est une plateforme de gestion terrain pour les opérations de NG-STARs. Interventions, clients, techniciens, rapports et administration.",
  },
};
