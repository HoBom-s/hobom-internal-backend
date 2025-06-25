CREATE TABLE message_delivery_histories (
    id 				BIGINT 			AUTO_INCREMENT PRIMARY KEY,
    type 			VARCHAR(32) 	NOT NULL,
    title 			VARCHAR(255) 	NOT NULL,
    body 			TEXT 			NOT NULL,
    recipient 		VARCHAR(255)   	NOT NULL,
    sender_id 		VARCHAR(128),
    sent_at 	  	DATETIME     	NOT NULL,
	created_at    	DATETIME     	NOT NULL  DEFAULT CURRENT_TIMESTAMP,
    updated_at    	DATETIME     	NOT NULL  DEFAULT CURRENT_TIMESTAMP
													ON UPDATE CURRENT_TIMESTAMP
);