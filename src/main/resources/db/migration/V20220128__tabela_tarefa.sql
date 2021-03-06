CREATE TABLE dbo.tb_task
(
	id BIGINT NOT NULL,
	id_user BIGINT NOT NULL,
	description VARCHAR(200) NOT NULL,
	due_date DATETIME NULL,
	create_date DATETIME NOT NULL,
	priority VARCHAR(10) NOT NULL,
	status VARCHAR(10) NOT NULL
);
	
ALTER TABLE dbo.tb_task ADD CONSTRAINT pktb_task
	PRIMARY KEY (id);

ALTER TABLE dbo.tb_task ADD CONSTRAINT fk_task_user
	FOREIGN KEY (id_user) REFERENCES dbo.tb_user (id)
	ON UPDATE NO ACTION ON DELETE NO ACTION;