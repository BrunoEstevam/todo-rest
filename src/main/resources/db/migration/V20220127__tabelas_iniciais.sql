CREATE TABLE dbo.tb_user
(
	id BIGINT NOT NULL,
	name VARCHAR(200) NOT NULL,
	email VARCHAR(200) NOT NULL,
	password VARCHAR(200) NOT NULL,
);

ALTER TABLE dbo.tb_user ADD CONSTRAINT PK_5
	PRIMARY KEY (id);

create sequence sq_user increment by 1;
