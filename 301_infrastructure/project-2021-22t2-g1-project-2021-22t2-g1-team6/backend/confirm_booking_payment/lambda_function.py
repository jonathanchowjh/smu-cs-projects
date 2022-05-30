
# pip install aurora-data-api -t .
# zip -r ../get-all-payment-packageUS.zip .

import aurora_data_api

db_name = 'hotelbookings'
db_cluster_arn = 'arn:aws:rds:us-east-1:922008661122:cluster:project-case4'
db_credentials_secrets_store_arn = 'arn:aws:secretsmanager:ap-southeast-1:922008661122:secret:rds-db-credentials/cluster-FQZYTOQUIXR7XKCFSBEA2RSYL4/admin-eE23kU'


def lambda_handler(event, context):

    return {
        'statusCode': 200,
        'body': "Success return"
    }
    # with aurora_data_api.connect(aurora_cluster_arn=db_cluster_arn, secret_arn=db_credentials_secrets_store_arn, database=db_name) as conn:
    #     with conn.cursor() as cursor:

    #         try:
    #             cursor.execute("select * from Bookings")

    #             return {
    #                 'statusCode': 200,
    #                 'body': cursor.fetchall()
    #             }

    #         except Exception as e:
    #             print("Oops!", e.__class__, "occurred.")
