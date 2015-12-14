#!/bin/sh

export ACCESS_KEY=AKIAJ2POCCPJ2RDFOXLQ
export SECRET_KEY=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

export BACKUP_FILE=robonews.backup.$(date "+%Y-%m-%d").sql.bz2
export OLD_BACKUP_FILE=robonews.backup.$(date -d yesterday "+%Y-%m-%d").sql.bz2

mysqldump --hex-blob -uroot robonews > temp.sql
bzip2 -9 temp.sql
rm -rf $BACKUP_FILE
mv temp.sql.bz2 $BACKUP_FILE

s3cmd --access_key=$ACCESS_KEY --secret_key=$SECRET_KEY put $BACKUP_FILE s3://backup.corp.robonews.io/$BACKUP_FILE
s3cmd --access_key=$ACCESS_KEY --secret_key=$SECRET_KEY del s3://backup.corp.robonews.io/$OLD_BACKUP_FILE
rm -rf $BACKUP_FILE