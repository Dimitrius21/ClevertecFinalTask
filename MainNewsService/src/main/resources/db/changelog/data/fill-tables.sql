--liquibase formatted sql
--changeset dmitri:fill_tables contextFilter:dev

INSERT INTO news (time, title, text, username) VALUES (localtimestamp, 'New BMW', 'Presented new BMW 5', 'Mark');
INSERT INTO comment (time, text, username, news_id) VALUES
    (localtimestamp, 'Comment11', 'Jon', 1),
   	(localtimestamp + interval '10 second', 'Comment12', 'Sema', 1),
   	(localtimestamp + interval '20 second', 'Comment13', 'Jon', 1),
   	(localtimestamp + interval '30 second', 'Comment14', 'Alice', 1),
   	(localtimestamp + interval '40 second', 'Comment15', 'Alice', 1),
   	(localtimestamp + interval '50 second', 'Comment16', 'Serge', 1),
   	(localtimestamp + interval '60 second', 'Comment17', 'Ivan', 1),
   	(localtimestamp + interval '70 second', 'Comment18', 'Jon', 1),
   	(localtimestamp + interval '80 second', 'Comment19', 'Maks', 1),
    (localtimestamp + interval '90 second', 'Comment110', 'Anton', 1),
    ('2023-05-30 00:00', 'Comment120', 'Maria', 1);

INSERT INTO news (time, title, text, username) VALUES (localtimestamp, 'Sport. Football. Italy', 'Napoli won Serie A', 'David');
INSERT INTO comment (time, text, username, news_id) VALUES
    (localtimestamp, 'Comment21', 'Jon', 2),
   	(localtimestamp + interval '10 second', 'Comment22', 'Sema', 2),
   	(localtimestamp + interval '20 second', 'Comment23', 'Jon', 2),
   	(localtimestamp + interval '30 second', 'Comment24', 'Alice', 2),
   	(localtimestamp + interval '40 second', 'Comment25', 'Alice', 2),
   	(localtimestamp + interval '50 second', 'Comment26', 'Serge', 2),
   	(localtimestamp + interval '60 second', 'Comment27', 'Ivan', 2),
   	(localtimestamp + interval '70 second', 'Comment28', 'Jon', 2),
   	(localtimestamp + interval '80 second', 'Comment29', 'Maks', 2),
    (localtimestamp + interval '90 second', 'Comment210', 'Anton', 2);


INSERT INTO news (time, title, text, username) VALUES (localtimestamp, 'Upgraded Toyota Yaris', 'An upgraded Toyota Yaris is presented in Europe', 'Mark');
INSERT INTO comment (time, text, username, news_id) VALUES
    (localtimestamp, 'Comment31', 'Jon', 3),
   	(localtimestamp + interval '10 second', 'Comment32', 'Sema', 3),
   	(localtimestamp + interval '20 second', 'Comment33', 'Jon', 3),
   	(localtimestamp + interval '30 second', 'Comment34', 'Alice', 3),
   	(localtimestamp + interval '40 second', 'Comment35', 'Alice', 3),
   	(localtimestamp + interval '50 second', 'Comment36', 'Serge', 3),
   	(localtimestamp + interval '60 second', 'Comment37', 'Ivan', 3),
   	(localtimestamp + interval '70 second', 'Comment38', 'Jon', 3),
   	(localtimestamp + interval '80 second', 'Comment39', 'Maks', 3),
    (localtimestamp + interval '90 second', 'Comment310', 'Anton', 3);


INSERT INTO news (time, title, text, username) VALUES (localtimestamp, 'The Cheapest tickets', 'Ryanair is going to cancel ticket by 9.99', 'Martin');
INSERT INTO comment (time, text, username, news_id) VALUES
    (localtimestamp, 'Comment41', 'Jon', 4),
   	(localtimestamp + interval '10 second', 'Comment42', 'Sema', 4),
   	(localtimestamp + interval '20 second', 'Comment43', 'Jon', 4),
   	(localtimestamp + interval '30 second', 'Comment44', 'Alice', 4),
   	(localtimestamp + interval '40 second', 'Comment45', 'Alice', 4),
   	(localtimestamp + interval '50 second', 'Comment46', 'Serge', 4),
   	(localtimestamp + interval '60 second', 'Comment47', 'Ivan', 4),
   	(localtimestamp + interval '70 second', 'Comment48', 'Jon', 4),
   	(localtimestamp + interval '80 second', 'Comment49', 'Maks', 4),
    (localtimestamp + interval '90 second', 'Comment410', 'Anton', 4);


INSERT INTO news (time, title, text, username) VALUES (localtimestamp, 'Event5', 'News5', 'Mark');
INSERT INTO comment (time, text, username, news_id) VALUES
    (localtimestamp, 'Comment51', 'Jon', 5),
   	(localtimestamp + interval '10 second', 'Comment52', 'Sema', 5),
   	(localtimestamp + interval '20 second', 'Comment53', 'Jon', 5),
   	(localtimestamp + interval '30 second', 'Comment54', 'Alice', 5),
   	(localtimestamp + interval '40 second', 'Comment55', 'Alice', 5),
   	(localtimestamp + interval '50 second', 'Comment56', 'Serge', 5),
   	(localtimestamp + interval '60 second', 'Comment57', 'Ivan', 5),
   	(localtimestamp + interval '70 second', 'Comment58', 'Jon', 5),
   	(localtimestamp + interval '80 second', 'Comment59', 'Maks', 5),
    (localtimestamp + interval '90 second', 'Comment510', 'Anton', 5);


INSERT INTO news (time, title, text, username) VALUES (localtimestamp, 'Event6', 'News6', 'David');
INSERT INTO comment (time, text, username, news_id) VALUES
    (localtimestamp, 'Comment61', 'Jon', 6),
   	(localtimestamp + interval '10 second', 'Comment62', 'Sema', 6),
   	(localtimestamp + interval '20 second', 'Comment63', 'Jon', 6),
   	(localtimestamp + interval '30 second', 'Comment64', 'Alice', 6),
   	(localtimestamp + interval '40 second', 'Comment65', 'Alice', 6),
   	(localtimestamp + interval '50 second', 'Comment66', 'Serge', 6),
   	(localtimestamp + interval '60 second', 'Comment67', 'Ivan', 6),
   	(localtimestamp + interval '70 second', 'Comment68', 'Jon', 6),
   	(localtimestamp + interval '80 second', 'Comment69', 'Maks', 6),
    (localtimestamp + interval '90 second', 'Comment610', 'Anton', 6);


INSERT INTO news (time, title, text, username) VALUES (localtimestamp, 'Event7', 'News7', 'Martin');
INSERT INTO comment (time, text, username, news_id) VALUES
    (localtimestamp, 'Comment71', 'Jon', 7),
   	(localtimestamp + interval '10 second', 'Comment72', 'Sema', 7),
   	(localtimestamp + interval '20 second', 'Comment73', 'Jon', 7),
   	(localtimestamp + interval '30 second', 'Comment74', 'Alice', 7),
   	(localtimestamp + interval '40 second', 'Comment75', 'Alice', 7),
   	(localtimestamp + interval '50 second', 'Comment76', 'Serge', 7),
   	(localtimestamp + interval '60 second', 'Comment77', 'Ivan', 7),
   	(localtimestamp + interval '70 second', 'Comment78', 'Jon', 7),
   	(localtimestamp + interval '80 second', 'Comment79', 'Maks', 7),
    (localtimestamp + interval '90 second', 'Comment710', 'Anton', 7);


INSERT INTO news (time, title, text, username) VALUES (localtimestamp, 'Event8', 'News8', 'Mark');
INSERT INTO comment (time, text, username, news_id) VALUES
    (localtimestamp, 'Comment81', 'Jon', 8),
   	(localtimestamp + interval '10 second', 'Comment82', 'Sema', 8),
   	(localtimestamp + interval '20 second', 'Comment83', 'Jon', 8),
   	(localtimestamp + interval '30 second', 'Comment84', 'Alice', 8),
   	(localtimestamp + interval '40 second', 'Comment85', 'Alice', 8),
   	(localtimestamp + interval '50 second', 'Comment86', 'Serge', 8),
   	(localtimestamp + interval '60 second', 'Comment87', 'Ivan', 8),
   	(localtimestamp + interval '70 second', 'Comment88', 'Jon', 8),
   	(localtimestamp + interval '80 second', 'Comment89', 'Maks', 8),
    (localtimestamp + interval '90 second', 'Comment810', 'Anton', 8);


INSERT INTO news (time, title, text, username) VALUES (localtimestamp, 'Event9', 'News9', 'Martin');
INSERT INTO comment (time, text, username, news_id) VALUES
    (localtimestamp, 'Comment91', 'Jon', 9),
   	(localtimestamp + interval '10 second', 'Comment92', 'Sema', 9),
   	(localtimestamp + interval '20 second', 'Comment93', 'Jon', 9),
   	(localtimestamp + interval '30 second', 'Comment94', 'Alice', 9),
   	(localtimestamp + interval '40 second', 'Comment95', 'Alice', 9),
   	(localtimestamp + interval '50 second', 'Comment96', 'Serge', 9),
   	(localtimestamp + interval '60 second', 'Comment97', 'Ivan', 9),
   	(localtimestamp + interval '70 second', 'Comment98', 'Jon', 9),
   	(localtimestamp + interval '80 second', 'Comment99', 'Maks', 9),
    (localtimestamp + interval '90 second', 'Comment910', 'Anton', 9),
    (localtimestamp + interval '100 second', 'Comment911', 'Elen', 9);


INSERT INTO news (time, title, text, username) VALUES (localtimestamp, 'Event10', 'News10', 'Martin');
INSERT INTO comment (time, text, username, news_id) VALUES
    (localtimestamp, 'Comment101', 'Jon', 10),
   	(localtimestamp + interval '10 second', 'Comment102', 'Sema', 10),
   	(localtimestamp + interval '20 second', 'Comment103', 'Jon', 10),
   	(localtimestamp + interval '30 second', 'Comment104', 'Alice', 10),
   	(localtimestamp + interval '40 second', 'Comment105', 'Alice', 10),
   	(localtimestamp + interval '50 second', 'Comment106', 'Serge', 10),
   	(localtimestamp + interval '60 second', 'Comment107', 'Ivan', 10),
   	(localtimestamp + interval '70 second', 'Comment108', 'Jon', 10),
   	(localtimestamp + interval '80 second', 'Comment109', 'Maks', 10),
    (localtimestamp + interval '90 second', 'Comment1010', 'Anton', 10);


INSERT INTO news (time, title, text, username) VALUES (localtimestamp, 'Event11', 'News11', 'Mark');
INSERT INTO comment (time, text, username, news_id) VALUES
        (localtimestamp, 'Comment111', 'Elen', 11),
        (localtimestamp + interval '100 second', 'Comment112', 'Elen', 1),
        (localtimestamp + interval '110 second', 'Comment113', 'Sema', 11),
        (localtimestamp + interval '120 second', 'Comment114', 'Jon', 11),
        (localtimestamp + interval '130 second', 'Comment115', 'Alice', 11),
        (localtimestamp + interval '140 second', 'Comment116', 'Gabriel', 11),
        (localtimestamp + interval '150 second', 'Comment117', 'Viktor', 11),
        (localtimestamp + interval '160 second', 'Comment118', 'Aleks', 11),
        (localtimestamp + interval '170 second', 'Comment119', 'Jimmy', 11),
        (localtimestamp + interval '180 second', 'Comment1110', 'Jon', 11);

INSERT INTO news (time, title, text, username) VALUES (localtimestamp, 'Event12', 'News12', 'David');
INSERT INTO comment (time, text, username, news_id) VALUES
    (localtimestamp + interval '100 second', 'Comment121', 'Elen', 12),
    (localtimestamp + interval '110 second', 'Comment122', 'Sema', 12),
    (localtimestamp + interval '120 second', 'Comment123', 'Jon', 12),
    (localtimestamp + interval '130 second', 'Comment124', 'Alice', 12),
    (localtimestamp + interval '140 second', 'Comment125', 'Gabriel', 12),
    (localtimestamp + interval '150 second', 'Comment126', 'Viktor', 12),
    (localtimestamp + interval '160 second', 'Comment127', 'Aleks', 12),
    (localtimestamp + interval '170 second', 'Comment128', 'Jimmy', 12),
    (localtimestamp + interval '180 second', 'Comment129', 'Jon', 12),
    (localtimestamp + interval '190 second', 'Comment1210', 'Maria', 12);

INSERT INTO news (time, title, text, username) VALUES (localtimestamp, 'Event13', 'News13', 'David');
INSERT INTO comment (time, text, username, news_id) VALUES
    (localtimestamp + interval '100 second', 'Comment131', 'Elen', 13),
    (localtimestamp + interval '110 second', 'Comment132', 'Sema', 13),
    (localtimestamp + interval '120 second', 'Comment133', 'Jon', 13),
    (localtimestamp + interval '130 second', 'Comment134', 'Alice', 13),
    (localtimestamp + interval '140 second', 'Comment135', 'Gabriel', 13),
    (localtimestamp + interval '150 second', 'Comment136', 'Viktor', 13),
    (localtimestamp + interval '160 second', 'Comment137', 'Aleks', 13),
    (localtimestamp + interval '170 second', 'Comment138', 'Jimmy', 13),
    (localtimestamp + interval '180 second', 'Comment129', 'Jon', 13),
    (localtimestamp + interval '190 second', 'Comment1310', 'Maria', 13);

INSERT INTO news (time, title, text, username) VALUES (localtimestamp, 'Event14', 'News14', 'Mark');
INSERT INTO comment (time, text, username, news_id) VALUES
    (localtimestamp + interval '100 second', 'Comment141', 'Elen', 14),
    (localtimestamp + interval '110 second', 'Comment142', 'Sema', 14),
    (localtimestamp + interval '120 second', 'Comment143', 'Jon', 14),
    (localtimestamp + interval '130 second', 'Comment144', 'Alice', 14),
    (localtimestamp + interval '140 second', 'Comment145', 'Gabriel', 14),
    (localtimestamp + interval '150 second', 'Comment146', 'Viktor', 14),
    (localtimestamp + interval '160 second', 'Comment147', 'Aleks', 14),
    (localtimestamp + interval '170 second', 'Comment148', 'Jimmy', 14),
    (localtimestamp + interval '180 second', 'Comment149', 'Jon', 14),
    (localtimestamp + interval '190 second', 'Comment1410', 'Maria', 14);

INSERT INTO news (time, title, text, username) VALUES (localtimestamp, 'Event15', 'News15', 'Mark');
INSERT INTO comment (time, text, username, news_id) VALUES
    (localtimestamp + interval '100 second', 'Comment151', 'Elen', 15),
    (localtimestamp + interval '110 second', 'Comment152', 'Sema', 15),
    (localtimestamp + interval '120 second', 'Comment153', 'Jon', 15),
    (localtimestamp + interval '130 second', 'Comment154', 'Alice', 15),
    (localtimestamp + interval '140 second', 'Comment155', 'Gabriel', 15),
    (localtimestamp + interval '150 second', 'Comment156', 'Viktor', 15),
    (localtimestamp + interval '160 second', 'Comment157', 'Aleks', 15),
    (localtimestamp + interval '170 second', 'Comment158', 'Jimmy', 15),
    (localtimestamp + interval '180 second', 'Comment159', 'Jon', 15),
    (localtimestamp + interval '190 second', 'Comment1510', 'Maria', 15);

INSERT INTO news (time, title, text, username) VALUES (localtimestamp, 'Event16', 'News16', 'Mark');
INSERT INTO comment (time, text, username, news_id) VALUES
    (localtimestamp + interval '100 second', 'Comment161', 'Elen', 16),
    (localtimestamp + interval '110 second', 'Comment162', 'Sema', 16),
    (localtimestamp + interval '120 second', 'Comment163', 'Jon', 16),
    (localtimestamp + interval '130 second', 'Comment164', 'Alice', 16),
    (localtimestamp + interval '140 second', 'Comment165', 'Gabriel', 16),
    (localtimestamp + interval '150 second', 'Comment166', 'Viktor', 16),
    (localtimestamp + interval '160 second', 'Comment167', 'Aleks', 16),
    (localtimestamp + interval '170 second', 'Comment168', 'Jimmy', 16),
    (localtimestamp + interval '180 second', 'Comment169', 'Jon', 16),
    (localtimestamp + interval '190 second', 'Comment1610', 'Maria', 16);

INSERT INTO news (time, title, text, username) VALUES (localtimestamp, 'Event17', 'News17', 'Martin');
INSERT INTO comment (time, text, username, news_id) VALUES
    (localtimestamp + interval '100 second', 'Comment171', 'Elen', 17),
    (localtimestamp + interval '110 second', 'Comment172', 'Sema', 17),
    (localtimestamp + interval '120 second', 'Comment173', 'Jon', 17),
    (localtimestamp + interval '130 second', 'Comment174', 'Alice', 17),
    (localtimestamp + interval '140 second', 'Comment175', 'Gabriel', 17),
    (localtimestamp + interval '150 second', 'Comment176', 'Viktor', 17),
    (localtimestamp + interval '160 second', 'Comment177', 'Aleks', 17),
    (localtimestamp + interval '170 second', 'Comment178', 'Jimmy', 17),
    (localtimestamp + interval '180 second', 'Comment179', 'Jon', 17),
    (localtimestamp + interval '190 second', 'Comment1710', 'Maria', 17);


INSERT INTO news (time, title, text, username) VALUES (localtimestamp, 'Event18', 'News18', 'Mark');
INSERT INTO comment (time, text, username, news_id) VALUES
    (localtimestamp + interval '100 second', 'Comment181', 'Elen', 18),
    (localtimestamp + interval '110 second', 'Comment182', 'Sema', 18),
    (localtimestamp + interval '120 second', 'Comment183', 'Jon', 18),
    (localtimestamp + interval '130 second', 'Comment184', 'Alice', 18),
    (localtimestamp + interval '140 second', 'Comment185', 'Gabriel', 18),
    (localtimestamp + interval '150 second', 'Comment186', 'Viktor', 18),
    (localtimestamp + interval '160 second', 'Comment187', 'Aleks', 18),
    (localtimestamp + interval '170 second', 'Comment188', 'Jimmy', 18),
    (localtimestamp + interval '180 second', 'Comment189', 'Jon', 18),
    (localtimestamp + interval '190 second', 'Comment1810', 'Maria', 18);

INSERT INTO news (time, title, text, username) VALUES (localtimestamp, 'Event19', 'News19', 'Martin');
INSERT INTO comment (time, text, username, news_id) VALUES
    (localtimestamp + interval '110 second', 'Comment192', 'Sema', 19),
    (localtimestamp + interval '120 second', 'Comment193', 'Jon', 19),
    (localtimestamp + interval '130 second', 'Comment194', 'Alice', 19),
    (localtimestamp + interval '140 second', 'Comment195', 'Gabriel', 19),
    (localtimestamp + interval '150 second', 'Comment196', 'Viktor', 19),
    (localtimestamp + interval '160 second', 'Comment197', 'Aleks', 19),
    (localtimestamp + interval '170 second', 'Comment198', 'Jimmy', 19),
    (localtimestamp + interval '180 second', 'Comment199', 'Jon', 19),
    (localtimestamp + interval '190 second', 'Comment1910', 'Maria', 19);

INSERT INTO news (time, title, text, username) VALUES (localtimestamp, 'Event20', 'News20', 'Martin');
INSERT INTO comment (time, text, username, news_id) VALUES
    (localtimestamp + interval '100 second', 'Comment201', 'Elen', 20),
    (localtimestamp + interval '110 second', 'Comment202', 'Sema', 20),
    (localtimestamp + interval '120 second', 'Comment203', 'Jon', 20),
    (localtimestamp + interval '130 second', 'Comment204', 'Alice', 20),
    (localtimestamp + interval '140 second', 'Comment205', 'Gabriel', 20),
    (localtimestamp + interval '150 second', 'Comment206', 'Viktor', 20),
    (localtimestamp + interval '160 second', 'Comment207', 'Aleks', 20),
    (localtimestamp + interval '170 second', 'Comment208', 'Jimmy', 20),
    (localtimestamp + interval '180 second', 'Comment209', 'Jon', 20),
    (localtimestamp + interval '190 second', 'Comment2010', 'Maria', 20);