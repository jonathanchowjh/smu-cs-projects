import React, { useEffect, useState } from "react";
import { useSearchParams, useLocation, useNavigate } from "react-router-dom";
import HotelCard from '../components/HotelCard';
import LoadingBar from "../components/LoadingBar";

const Page = (props) => {
  const [searchParams] = useSearchParams();
  const [state, setState] = useState(defaultState);
  let location = useLocation();
  const navigate = useNavigate();
  useEffect(() => {
    // PARAM CHECK
    // lengthOfList 10 / 20 -> maxList response
    console.log("destination_id: " + searchParams.get("destination_id"));
    console.log("checkin: " + searchParams.get("checkin"));
    console.log("checkout: " + searchParams.get("checkout"));
    console.log("lang: " + searchParams.get("lang"));
    console.log("currency: " + searchParams.get("currency"));
    console.log("country_code: " + searchParams.get("country_code"));
    console.log("guests: " + searchParams.get("guests"));
    console.log("partner_id: " + searchParams.get("partner_id"));

    // FETCH HOTELS
    loadHotels();
  }, [])

  // LOAD HOTELS
  const loadHotels = () => {
    if (state.hotelList.data.length >= state.hotelList.maxLength) return;
    if (!state.pageLoading) { setState({ ...state, loading: true }); }
    const url = `https://peql3n5ie0.execute-api.ap-southeast-1.amazonaws.com/getHotel${location.search}`
    + `&listLength=${state.listLength * 2}`;
    console.log(`FETCH HOTELS: ${url}`);
    fetch(url).then((response) => {
      return response.json()
    }).then((res) => {
      console.log(res.data);
      setState({
        ...state,
        hotelList: res,
        listLength: res.data.length,
        loading: false,
        pageLoading: false
      });
    });
  }
  
  return (
    <div style={{ width: "70%", margin: "0 auto", maxHeight: "90vh", overflowY: "scroll" }}>
      <h2 className="mx-2 my-5">
        HOTELS: {searchParams.get("destination_id")}
      </h2>
      {
        state && state.hotelList && state.hotelList.data
        && state.hotelList.data.map((ele) => {
          return (
            <HotelCard
              loading={state.pageLoading}
              key={ele.hotel_id}
              title={ele.name}
              text={ele.address}
              textTwo={`Rating: ${ele.rating} / 5`}
              textThree={`Price: $${ele.lowest_price}`}
              image_link={ele.image_link}
              onClick={() => navigate(`/hotel/${ele.hotel_id}${location.search}`)}
            />
          )
        })
      }
      <div className="d-flex justify-content-center mb-5">
        {
          state.loading || state.pageLoading ? <LoadingBar /> : (
            <button
              className={`btn btn-primary btn-lg ${state.hotelList.data.length >= state.hotelList.maxLength ? "disabled" : ""}`}
              aria-disabled="true"
              onClick={loadHotels}
            >
              Load More...
            </button>
          )
        }
      </div>  
      {/* <button onClick={() => console.log(state)}>
        console
      </button> */}
    </div>
  )
}

export default Page;

const defaultState = {
  listLength: 5,
  loading: false,
  pageLoading: true,
  hotelList: {
    maxLength: 200,
    data: [
      {
        hotel_id: "XLBP",
        name: "The Hive Singapore Hostel",
        latitude: 1.315977,
        longitude: 103.858832,
        address: "624 Serangoon Road",
        rating: 2,
        image_link: "https://d2ey9sqrvkqdfs.cloudfront.net/XLBP/0.jpg",
        lowest_price: 39.06
      },
      {
        hotel_id: "VF3U",
        name: "Amrise",
        latitude: 1.31403,
        longitude: 103.87781,
        address: "112 Sims Avenue",
        rating: 1,
        image_link: "https://d2ey9sqrvkqdfs.cloudfront.net/VF3U/0.jpg",
        lowest_price: 52.79
      },
      {
        hotel_id: "jzzR",
        name: "Hotel 81 Geylang",
        latitude: 1.311821,
        longitude: 103.87975,
        address: "20 Lorong 16 Geylang",
        rating: 2,
        image_link: "https://d2ey9sqrvkqdfs.cloudfront.net/jzzR/0.jpg",
        lowest_price: 54.32
      },
      {
        hotel_id: "Hwwg",
        name: "Hotel 81 Palace",
        latitude: 1.31188,
        longitude: 103.88,
        address: "25 Lorong 16 Geylang",
        rating: 2,
        image_link: "https://d2ey9sqrvkqdfs.cloudfront.net/Hwwg/0.jpg",
        lowest_price: 54.38
      },
      {
        hotel_id: "IqwD",
        name: "Hotel 81 Sakura",
        latitude: 1.31225788593292,
        longitude: 103.900436401367,
        address: "181 Joo Chiat Road",
        rating: 2,
        image_link: "https://d2ey9sqrvkqdfs.cloudfront.net/IqwD/0.jpg",
        lowest_price: 54.76
      }
    ]
  }
}