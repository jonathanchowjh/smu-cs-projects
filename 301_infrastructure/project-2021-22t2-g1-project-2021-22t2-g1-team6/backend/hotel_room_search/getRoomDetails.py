import boto3
import json
import urllib3

lambda_client = boto3.client('lambda')


def lambda_handler(event, context):
    destination_id=event['destination_id']
    checkin=event['checkin']
    checkout=event['checkout']
    lang=event['lang']
    currency=event['currency']
    country_code=event['country_code']
    guests=event['guests']
    partner_id=event['partner_id']
    # listLength=event['listLength']
    hotel_id=event['hotel_id']
    callno=event['callno']

    query_param={'destination_id': destination_id, 'checkin': checkin,
            'checkout':checkout , 'lang':lang,
             'currency':currency, 'country_code':country_code,'hotel_id':hotel_id,
             'guests': guests,'partner_id':'1'}
    
    if callno not in ['get_ean_price', 'get_bedcom', 'get_wgl']:
        return "error: wrong callno"
    invoke_response1 = lambda_client.invoke(FunctionName=callno,
                                          InvocationType='RequestResponse',
                                          Payload = json.dumps(query_param))
    
    priceEncoded = invoke_response1['Payload'].read()
    return priceEncoded