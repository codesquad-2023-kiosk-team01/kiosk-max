ROOT_DIR = “/home/ubuntu/front”
S3_BUCKET=“s3://kiosk-team01-actions-front-bucket”

mkdir -p ${ROOT_DIR}
aws s3 cp ${S3_BUCKET}/ .

sudo service nginx restart