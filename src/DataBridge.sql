CREATE DATABASE DataBridge;
USE DataBridge;

-- EERD model, including super type and sub type
-- "User-related" part
CREATE TABLE Recipients
(
	receiver_id BIGINT PRIMARY KEY NOT NULL,
	public_id BIGINT UNIQUE NOT NULL,
	type CHAR NOT NULL
)

-- Plural table naming due to "User" keyword collision
CREATE TABLE Users
(
	user_id BIGINT PRIMARY KEY NOT NULL,
	username VARCHAR(30) UNIQUE NOT NULL,
	profile NVARCHAR(3000),
	is_banned BINARY NOT NULL,
	is_private BINARY NOT NULL,
	reputation INT,
	CONSTRAINT FK_user_recipient_id 
		FOREIGN KEY(user_id)
		REFERENCES Recipients(receiver_id) ON DELETE CASCADE,
)

-- ALTER TABLE Users ALTER COLUMN username VARCHAR(30) NOT NULL
-- ALTER TABLE Users ADD CONSTRAINT UniqueUsername UNIQUE(username)

CREATE TABLE Passwords
(
	username VARCHAR(30) PRIMARY KEY REFERENCES Users(username),
	salt CHAR(32) NOT NULL,
	hashed_password CHAR(64) NOT NULL
)

CREATE TABLE Friendship
(
	user_A BIGINT REFERENCES Users(user_id) NOT NULL,
	user_B BIGINT REFERENCES Users(user_id) NOT NULL,
	attitude_A TINYINT,
	attitude_B TINYINT,
	PRIMARY KEY(user_A, user_B)
)

CREATE TABLE Groups
(
	group_id BIGINT PRIMARY KEY REFERENCES Recipients(receiver_id) NOT NULL,
	name NCHAR(30) NOT NULL
)

CREATE TABLE GroupMembership
(
	group_id BIGINT REFERENCES Groups(group_id) NOT NULL,
	user_id BIGINT REFERENCES Users(user_id) NOT NULL,
	PRIMARY KEY(group_id, user_id)
)


-- "File-related" part
CREATE TABLE Files
(
	file_id CHAR(30) PRIMARY KEY NOT NULL,
	uploader BIGINT REFERENCES Users(user_id) NOT NULL,
	parent_id CHAR(30), 
	-- allow null for root folders and files
	filename NVARCHAR(80) NOT NULL,
	notes NVARCHAR(100),
	is_folder BINARY(1) NOT NULL,
	is_private BINARY(1) DEFAULT(1) NOT NULL,
	created_at DATETIME NOT NULL,
)

CREATE TABLE Votes
(
	user_id BIGINT REFERENCES Users(user_id) NOT NULL,
	file_id CHAR(30) REFERENCES Files(file_id) NOT NULL,
	vote TINYINT NOT NULL,
	PRIMARY KEY(user_id, file_id)
)

CREATE TABLE Comments
(
	user_id BIGINT REFERENCES Users(user_id) NOT NULL,
	file_id CHAR(30) REFERENCES Files(file_id) NOT NULL,
	content NVARCHAR(100) NOT NULL,
	created_at DATETIME NOT NULL
)

-- Message-related part

CREATE TABLE Messages
(
	message_id INT IDENTITY(1,1) PRIMARY KEY NOT NULL,
	sender BIGINT REFERENCES Users(user_id) NOT NULL,
	receiver BIGINT REFERENCES Recipients(receiver_id) NOT NULL,
	is_file BINARY(1) NOT NULL,
	sent_at DATETIME NOT NULL
)

CREATE TABLE NormalMessages
(
	message_id INT IDENTITY(1,1) PRIMARY KEY REFERENCES Messages(message_id) NOT NULL,
	content NVARCHAR(500) NOT NULL,
)

CREATE TABLE FileLinks
(
	message_id INT IDENTITY(1,1) PRIMARY KEY REFERENCES Messages(message_id) NOT NULL,
	file_id CHAR(30) REFERENCES Files(file_id) NOT NULL
)

INSERT INTO Recipients VALUES(1,-1, 'U')
INSERT INTO Users VALUES(1, 'test', 'I am Groot', 0, 0, 0)
INSERT INTO Passwords VALUES('test', 'salt', 'hashed_password')