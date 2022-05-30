import React, { useEffect, useState } from "react";
import {
  useParams,
  useLocation,
  useSearchParams,
  useNavigate,
} from "react-router-dom";

import RoomListing from "../components/RoomListing";

const Page = () => {
  const [searchParams] = useSearchParams();
  const [state, setState] = useState(defaultState);
  const { id } = useParams(); // get hotel_id
  let location = useLocation();
  const navigate = useNavigate();
  //console.log(reqQuery);
  useEffect(() => {
    setState({
      ...state,
      hotel_id: id
    })
    const reqBody = {
      destination_id: searchParams.get("destination_id"),
      checkin: searchParams.get("checkin"),
      checkout: searchParams.get("checkout"),
      lang: searchParams.get("lang"),
      currency: searchParams.get("currency"),
      country_code: searchParams.get("country_code"),
      guests: searchParams.get("guests"),
      partner_id: searchParams.get("partner_id"),
      hotel_id: id,
      callno: 'get_wgl'
    };
    // get_ean_price get_bedcom get_wgl
    loadRoomsContainer(reqBody);
  }, []);

  const loadRoomsContainer = async (reqBody) => {
    let roomsTotal = []

    const roomsOne = await loadRooms(reqBody, 'get_ean_price');
    roomsTotal = filterRooms(roomsOne, roomsTotal);
    setState({ ...state, rooms: roomsTotal });

    const roomsTwo = await loadRooms(reqBody, 'get_bedcom');
    roomsTotal = filterRooms(roomsTwo, roomsTotal);
    setState({ ...state, rooms: roomsTotal });

    const roomsThree = await loadRooms(reqBody, 'get_wgl');
    roomsTotal = filterRooms(roomsThree, roomsTotal);
    setState({ ...state, rooms: roomsTotal });
  }

  const loadRooms = async (reqBody, callno) => {
    const url = "https://8c610qr392.execute-api.ap-southeast-1.amazonaws.com/dev";
    reqBody.callno = callno;
    
    const response = await fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(reqBody),
    });
    const res = await response.json();

    console.log(JSON.parse(res));
    if (JSON.parse(res)["rooms"] === []) {
      console.log("Pricing API Error");
    }
    let rooms = formatRoomData(JSON.parse(res)["rooms"]);
    return rooms;
  };

  const filterRooms = (rooms, roomsTotal) => {
    // console.log(rooms, roomsTotal)
    const roomsWithChanges = rooms.filter((newRoom) => {
      const getCurrentRoom = roomsTotal.filter((currentRoom) => currentRoom.key === newRoom.key)
      if (getCurrentRoom.length === 0 || getCurrentRoom[0].price > newRoom.price) return true
      return false
    })
    return roomsTotal
      .filter((oldRoom) => !roomsWithChanges.some((ele) => ele.key === oldRoom.key))
      .concat(roomsWithChanges)
  }

  const generateRandomImgURL = (images) => {
    if (images.length > 0) {
      return images[Math.floor(Math.random() * images.length)];
    } 
    return ""
    
  };
  const formatRoomData = (result) => {
    var allFormattedRoomData = [];
    result.map((item, i) => {
      let formattedRoomData = {
        amenities: item.amenities, // list of amenities
        points: item.points,
        price: item.price,
        roomNormalizedDescription: item.roomNormalizedDescription,
        image: generateRandomImgURL(item.images),
        //image: item.images[0],
        key: item.key,
      };
      allFormattedRoomData.push(formattedRoomData);
    });
    return allFormattedRoomData;
  };
  //const { id } = useParams();
  // get all the rooms related to a hotel using hotel_id (axios call to backend)
  //
  // useEffect(() => {
  //   console.log("hotel_id: " + id);
  // }, []);

  //const mock_rooms_data = {};
  console.log(state.rooms);

  return (
    <div>
      {/* title of page */}
      <h2 className="mx-2 my-5">CHECK OUT OUR FANCY ROOMS!!!</h2>

      {/* room listings */}

      {state.rooms === [] ? (
        <></>
      ) : (
        state.rooms.map((item, i) => (
          <RoomListing
            room_data={item}
            key={item.key}
            loading={false}
            onClick={() => navigate(`/hotel/${id}/${item.key}${location.search}&price=${item.price}`)}
          />
        ))
      )}
    </div>
  );
};

export default Page;

const defaultState = {
  // reqBody: {
  //   destination_id: "",
  //   checkin: "",
  //   checkout: "",
  //   lang: "",
  //   currency: "",
  //   country_code: "",
  //   guests: "",
  //   partner_id: "",
  //   hotel_id: "",
  // },
  rooms: [],
  pageLoading: true,
  hotel_id: "",
  room_id:"",
};
