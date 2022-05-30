
# pip install aurora-data-api -t .
# zip -r ../get-all-payment-package.zip .

import aurora_data_api

db_name = 'hotelbookings'
db_cluster_arn = 'arn:aws:rds:ap-southeast-1:922008661122:cluster:hotelbookingproduction'
db_credentials_secrets_store_arn = 'arn:aws:secretsmanager:ap-southeast-1:922008661122:secret:rds-db-credentials/cluster-5H5IMQTQSKFOFEQPTL35Y7K5XQ/admin-JqPhMZ'


def lambda_handler(event, context):

    with aurora_data_api.connect(aurora_cluster_arn=db_cluster_arn, secret_arn=db_credentials_secrets_store_arn, database=db_name) as conn:
        with conn.cursor() as cursor:

            try:
                cursor.execute("select * from Bookings")

                return {
                    'statusCode': 200,
                    'body': cursor.fetchall()
                }

            except Exception as e:
                print("Oops!", e.__class__, "occurred.")
