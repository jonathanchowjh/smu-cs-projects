from flask import Flask,request,jsonify
import requests
import os

host = "https://hotelapi.loyalty.dev/api/"

headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}


app = Flask(__name__)

@app.route("/get_hotels_by_locations",methods=['GET'])

def get_hotels_by_locations():

    url = 'hotels/prices'

    input_info=request.get_json()

    destination_id=input_info['destination_id']
    checkin=input_info['checkin']
    checkout=input_info['checkout']
    lang=input_info['lang']
    currency=input_info['currency']
    country_code=input_info['country_code']
    guests=input_info['guests']

    url = 'hotels/prices'
    query_param={'destination_id': destination_id, 'checkin': checkin,
            'checkout':checkout , 'lang':lang,
             'currency':currency, 'country_code':country_code,
             'guests': guests,'partner_id':'1'}

    
    hotel_details = requests.get(host + url, params=query_param).json()

    all_hotels=hotel_details['hotels']
    lowest_price=[]
    for h in all_hotels:
        lowest_price.append([h['id'],h['lowest_price']])

    lowest_price = sorted(lowest_price, key=lambda x: x[1], reverse=True)

    hotel_names=[]
    lowest=[]
    for i in lowest_price:
        hotel_names.append(i[0])
        lowest.append(i[1])

    return  jsonify(
        {
         "hotel_names":hotel_names,
        "lowest_price":lowest
        }
    )





if __name__ == "__main__":
    port = int(os.environ.get('PORT', 5000))
    app.run(host = '0.0.0.0', port = port)
