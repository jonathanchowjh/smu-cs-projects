import urllib3
import pandas as pd
import json

GET_HOTEL_PATH='/getHotel'

def lambda_handler(event, context):
    
    if event['rawPath']==GET_HOTEL_PATH:

        destination_id=event['queryStringParameters']['destination_id']
        checkin=event['queryStringParameters']['checkin']
        checkout=event['queryStringParameters']['checkout']
        lang=event['queryStringParameters']['lang']
        currency=event['queryStringParameters']['currency']
        country_code=event['queryStringParameters']['country_code']
        guests=event['queryStringParameters']['guests']
        partner_id=event['queryStringParameters']['partner_id']
        listLength=event['queryStringParameters']['listLength']
        

        host = "https://hotelapi.loyalty.dev/api/"
        headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
        url = 'hotels/prices'
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
                             
        hotel_details = json.loads(response.data)
        
        all_hotels=hotel_details['hotels']

        #get price
        lowest_price=[]
        for h in all_hotels:
            lowest_price.append([h['id'],h['lowest_price']])
    
        lowest_price = sorted(lowest_price, key=lambda x: x[1], reverse=True)
        lowest_price_df=pd.DataFrame(lowest_price,columns=['hotel_id','lowest_price'])
    
        #get hotel details
        url = 'hotels'
        query_param={'destination_id': destination_id}
        
        response = http.request('GET',
                            host + url,
                            fields=query_param,
                            headers = {'Content-Type': 'application/json'},
                 )
        
    
        des_details = json.loads(response.data)

        hotel_details=[]
        for details in des_details:
    
            image_link=details['image_details']['prefix']+'0.jpg'
            hotel_details.append([details['id'],details['name'],details['latitude'],details['longitude'],details['address'],
                                  details['rating'],image_link
                                 ])
    
    
        hotel_details_df=pd.DataFrame(hotel_details,columns=['hotel_id','name','latitude','longitude','address','rating','image_link'])
    
        #get result
        combined_df=pd.merge(hotel_details_df,lowest_price_df,on='hotel_id')
        combined_df=combined_df.sort_values(by=['lowest_price'],ascending=True)
        truncated_df=combined_df[:int(listLength)]
        
        #dump json
        df_dict = truncated_df.to_dict(orient='records')
        myDict = {
            'data': df_dict,
            'maxLength': len(combined_df)
        }
        return_json = json.dumps(myDict)
        
        return return_json
        