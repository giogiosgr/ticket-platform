INSERT INTO `tickets` (`title`, `description`, `category`, `status`, `created_at`, `updated_at`) VALUES ('Aiuto con gestionale SOAP','SOAP non parte più dopo aggiornamento di Windows','Gestionale','da fare','2024-08-28 20:00:00','2024-08-28 20:00:00'),('Problema PC','Non si accende più nonostante la spina sia ben attaccata','Hardware','da fare','2024-08-28 20:00:00','2024-08-28 20:00:00'),('Dati su SOAP','Non trovo più i miei dati di fatturazione','Gestionale','da fare','2024-08-28 20:00:00','2024-08-28 20:00:00');
INSERT INTO `user` (`username`, `password`, `email`, `status`,`created_at`, `updated_at`) VALUES ('admin', '{noop}q', "admin@ticketplatform.org",1,'2024-08-28 20:00:00','2024-08-28 20:00:00');
INSERT INTO `role` (`name`) VALUES ('ADMIN'),('OPERATOR');
INSERT INTO `user_roles` (`user_id`, `roles_id`) VALUES (1, 1);