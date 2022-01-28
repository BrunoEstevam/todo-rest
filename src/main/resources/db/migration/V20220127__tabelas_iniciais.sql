CREATE TABLE dbo.tb_role
(
	id BIGINT NOT NULL,
	name VARCHAR(100) NOT NULL,
	description VARCHAR(200) NULL,
	code VARCHAR(50) NOT NULL
);

ALTER TABLE dbo.tb_role ADD CONSTRAINT pktb_role
	PRIMARY KEY (id);

CREATE TABLE dbo.tb_role_user
(
	id_role BIGINT NOT NULL,
	id_user BIGINT NOT NULL
);

CREATE TABLE dbo.tb_user
(
	id BIGINT NOT NULL,
	name VARCHAR(200) NOT NULL,
	email VARCHAR(200) NOT NULL,
	password VARCHAR(200) NOT NULL,
	status CHAR(3) NOT NULL
);
ALTER TABLE dbo.tb_user ADD CONSTRAINT PK_5
	PRIMARY KEY (id);

ALTER TABLE dbo.tb_role_user ADD CONSTRAINT fk_role_user
	FOREIGN KEY (id_role) REFERENCES dbo.tb_role (id)
	ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE dbo.tb_role_user ADD CONSTRAINT fk_user_role
	FOREIGN KEY (id_user) REFERENCES dbo.tb_user (id)
	ON UPDATE NO ACTION ON DELETE NO ACTION;
	
create sequence sq_role increment by 1;
create sequence sq_user increment by 1;
