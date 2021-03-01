--
-- Licensed to the Apache Software Foundation (ASF) under one or more
-- contributor license agreements.  See the NOTICE file distributed with
-- this work for additional information regarding copyright ownership.
-- The ASF licenses this file to You under the Apache License, Version 2.0
-- (the "License"); you may not use this file except in compliance with
-- the License.  You may obtain a copy of the License at
--
--     http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

CREATE USER IF NOT EXISTS 'root'@'%' IDENTIFIED BY '';
GRANT All privileges ON *.* TO 'root'@'%';

SET character_set_database='utf8';
SET character_set_server='utf8';

DROP DATABASE IF EXISTS encrypt;

CREATE DATABASE encrypt;

CREATE TABLE encrypt.t_user (user_id INT NOT NULL, pwd_plain VARCHAR(45) NULL, pwd_cipher VARCHAR(45) NULL, PRIMARY KEY (user_id));
CREATE TABLE encrypt.t_order (order_id INT NOT NULL, user_id INT NOT NULL, status VARCHAR(45) NULL, PRIMARY KEY (order_id));
CREATE TABLE encrypt.t_order_item (item_id INT NOT NULL, order_id INT NOT NULL, user_id INT NOT NULL, status VARCHAR(45) NULL, creation_date DATE, PRIMARY KEY (item_id));
CREATE TABLE encrypt.t_broadcast_table (id INT NOT NULL, status VARCHAR(45) NULL, PRIMARY KEY (id));
CREATE INDEX user_index_t_user ON encrypt.t_user (user_id);
CREATE INDEX order_index_t_order ON encrypt.t_order (order_id);
