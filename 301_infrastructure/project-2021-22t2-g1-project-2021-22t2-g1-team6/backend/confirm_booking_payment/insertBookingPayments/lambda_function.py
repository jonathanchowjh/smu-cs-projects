
# pip install aurora-data-api -t .
# zip -r ../insert-booking-payment-package.zip .
import json
import aurora_data_api
import uuid

db_name = 'hotelbookings'
db_cluster_arn = 'arn:aws:rds:ap-southeast-1:922008661122:cluster:hotelbookingproduction'
db_credentials_secrets_store_arn = 'arn:aws:secretsmanager:ap-southeast-1:922008661122:secret:rds-db-credentials/cluster-5H5IMQTQSKFOFEQPTL35Y7K5XQ/admin-JqPhMZ'


def lambda_handler(event, context):
    with aurora_data_api.connect(aurora_cluster_arn=db_cluster_arn, secret_arn=db_credentials_secrets_store_arn, database=db_name) as conn:
        with conn.cursor() as cursor:
            booking_id = str(uuid.uuid4())
            destination_id = event['destination_id']
            hotel_id = event['hotel_id']
            hotel_room_id = event['hotel_room_id']
            price = event['price']
            start_date = event['start_date']
            end_date = event['end_date']
            cust_firstname = event['cust_firstname']
            cust_lastname = event['cust_lastname']
            cust_email = event['cust_email']

            if(len(destination_id) == 0 or len(hotel_id) == 0 or len(hotel_room_id) == 0 or len(price) == 0 or len(start_date) == 0 or len(end_date) == 0 or len(cust_firstname) == 0 or len(cust_lastname) == 0 or len(cust_email) == 0):
                return {
                    'statusCode': 400,
                    'message': "Empty value detected"
                }
            else:
                try:
                    sql = '''INSERT INTO Bookings (booking_id, destination_id, hotel_id, hotel_room_id, price, start_date, end_date, cust_firstname, cust_lastname, cust_email) VALUES ("%s", "%s", "%s", "%s", "%s", "%s", "%s", "%s", "%s", "%s")''' % (
                        booking_id, destination_id, hotel_id, hotel_room_id, price, start_date, end_date, cust_firstname, cust_lastname, cust_email)
                    cursor.execute(sql)
                    conn.commit()

                    return {
                        'statusCode': 200,
                        'body': booking_id
                    }

                except Exception as e:
                    print("Oops!", e.__class__, "occurred.")
