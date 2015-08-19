#!/bin/sh
mysqldump --no-data -uroot -proot  news-test > ~/Dropbox/news/core/src/test/resources/sql/db-schema.sql
#mysqldump --no-create-info --complete-insert -uroot -proot  news-test > ~/Dropbox/news/core/src/test/resources/sql/db-data.sql

#mysqldump --compatible=ansi --no-data -uroot -proot  news-test > ~/Dropbox/news/core/src/test/resources/sql/db-schema2.sql