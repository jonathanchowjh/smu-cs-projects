
# pip install aurora-data-api -t .
# zip -r ../my-deployment-package.zip .

import aurora_data_api

db_name = 'Bookings'
db_cluster_arn = 'arn:aws:rds:ap-southeast-1:922008661122:cluster:hotelbookingcluster'
db_credentials_secrets_store_arn = 'arn:aws:secretsmanager:ap-southeast-1:922008661122:secret:rds-db-credentials/cluster-VOX25SR43WA4YT7V5IENNTIEUQ/itsa_admin_g1t6-sB6moo'


def lambda_handler(event, context):
    with aurora_data_api.connect(aurora_cluster_arn=db_cluster_arn, secret_arn=db_credentials_secrets_store_arn, database=db_name) as conn:
        with conn.cursor() as cursor:

            try:

                sql = "select * from Bookings where booking_id = %s"
                cursor.execute(sql, (1))

                return cursor.fetchone()

            except Exception as e:
                print("Oops!", e.__class__, "occurred.")
