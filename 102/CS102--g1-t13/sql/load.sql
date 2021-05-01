
load data local infile '/Users/hartonotjakrawinata/schoolCode/102/CS102--g1-t13/sql/data/user.csv' into table users
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES
(username, name, password, gold, xp);

load data local infile '/Users/hartonotjakrawinata/schoolCode/102/CS102--g1-t13/sql/data/friends.csv' into table friends
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES
(u1, u2);

load data local infile '/Users/hartonotjakrawinata/schoolCode/102/CS102--g1-t13/sql/data/friend_requests.csv' into table friend_requests
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES
(sender, receipient);

load data local infile '/Users/hartonotjakrawinata/schoolCode/102/CS102--g1-t13/sql/data/posts.csv' into table posts
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES
(pid,username,dst_username,post,dt);

load data local infile '/Users/hartonotjakrawinata/schoolCode/102/CS102--g1-t13/sql/data/lands.csv' into table lands
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES
(lid, username, crop, startTime);

load data local infile '/Users/hartonotjakrawinata/schoolCode/102/CS102--g1-t13/sql/data/likes.csv' into table likes
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES
(username, pid, isLike);

load data local infile '/Users/hartonotjakrawinata/schoolCode/102/CS102--g1-t13/sql/data/seeds.csv' into table seeds
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES
(username,crop,quantity);

load data local infile '/Users/hartonotjakrawinata/schoolCode/102/CS102--g1-t13/sql/data/gifts.csv' into table gifts
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES
(gid,gifter,giftee,crop,accepted,dt);

load data local infile '/Users/hartonotjakrawinata/schoolCode/102/CS102--g1-t13/sql/data/replies.csv' into table replies
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES
(rid,pid,username,reply,dt);

-- INSERT INTO posts (username, post, dst_username, dt) VALUES ('jon','hello, Im jon!','2020-01-01 13:01:52');