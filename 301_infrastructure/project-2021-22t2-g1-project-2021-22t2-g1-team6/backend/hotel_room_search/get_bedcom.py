import urllib3
import pandas as pd
import json
import random
import time

def lambda_handler(event, context):
    
    wait_time=random.randrange(2, 18)
    time.sleep(wait_time)
    
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
    
    
    host = "https://hotelapi.loyalty.dev/api/"
    headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    url = 'hotels/{hid}/price'.format(hid=hotel_id)

    query_param={'destination_id': destination_id, 'checkin': checkin,
                'checkout':checkout , 'lang':lang,
                 'currency':currency, 'country_code':country_code,
                 'guests': guests,'partner_id':'1'}
    
    http = urllib3.PoolManager()
    response = http.request('GET',
                        host + url,
                        fields=query_param,
                        headers = {'Content-Type': 'application/json'},
             )

    room_details = json.loads(response.data)

    
    result=[]

    for room in room_details['rooms']:
        tmp_dict=room
        tmp_dict['key']=room['key']

        random_value=random.randrange(800, 1100)/1000
        tmp_dict['price']=round(room['price']*random_value,2)

        result.append(tmp_dict)

    json_dict = {
        "rooms": result,
        "supplier":'BEDCOM',
        "time_elapsed": str(wait_time)
    }

    
    return_json = json.dumps(json_dict, default=str)
        
    return return_json