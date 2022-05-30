import React, { useState, useEffect } from "react";
import {
  useParams,
  useLocation,
  useSearchParams,
  useNavigate,
} from "react-router-dom";

import moment from "moment";
import { makeStyles } from "@material-ui/core/styles";

const Page = () => {
  const { id, room } = useParams();
  const [state, setState] = useState(bookingState);
  const [searchParams] = useSearchParams();
  let location = useLocation();
  const navigate = useNavigate();
  var reqQuery = location.search;
  const form_data = {
    destination_id: searchParams.get("destination_id"),
    start_date: searchParams.get("checkin"), // from previous page
    end_date: searchParams.get("checkout"),
    price: searchParams.get("price"),
    hotel_id: id.toString(),
    hotel_room_id: room.toString(),
  };
  useEffect(() => {
    // setState({
    //   form: {
    //     ...state.form,
    //     destination_id: searchParams.get("destination_id"),
    //     start_date: searchParams.get("checkin"), // from previous page
    //     end_date: searchParams.get("checkout"),
    //     price: searchParams.get("price"),
    //     hotel_id: id.toString(),
    //     hotel_room_id: room.toString(),
    //   },
    //   ...state,
    // });
    // console.log(form_data);
    // console.log(state);
  }, []);


  const useStyles = makeStyles((theme) => ({
    root: {
      display: "flex",
      flexWrap: "wrap",
    },
    textField: {
      marginLeft: theme.spacing(1),
      marginRight: theme.spacing(1),
      width: "25ch",
    },
  }));

  const classes = useStyles();

  const cust_salutations = ["Mr", "Mrs", "Ms", "Dr"];

  // handle form excluding payments
  const handleForm = (attrName) => (e) => {
    const val = e.target ? e.target.value : e;
    setState({
      ...state,
      form: {
        ...state.form,
        [attrName]: val,
      },
    });
  };

  // handle payments
  const handlePayments = (attrName) => (e) => {
    const val = e.target ? e.target.value : e;
    setState({
      ...state,
      payments: {
        ...state.payments,
        [attrName]: val,
      },
    });
  };

  // handle writing booking into backend
  const submitBooking = () => {
    console.log("Sending booking data to backend");
    const data = {
      // destination_id: form.destination_id.toString(),
      // hotel_id: form.hotel_id.toString(),
      // hotel_room_id: form.hotel_room_id.toString(),
      // price: form.price.toString(),
      // start_date: form.start_date.toString(),
      // end_date: form.end_date.toString(),
      ...form_data,
      cust_firstname: state.form.cust_firstname.toString(),
      cust_lastname: state.form.cust_lastname.toString(),
      cust_email: state.form.cust_email.toString(),
    };
    console.log(data);
    const url =
      "https://00gk46nd3g.execute-api.ap-southeast-1.amazonaws.com/deployment/insertBookingPayments";
    var result = "";
    fetch(url, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    })
      .then((response) => {
        return response.json();
      })
      .then((body) => {
        console.log(body);
        if (body !== null & body["statusCode"] == 200) {
          setState({
            ...state,
            show_submit_booking_success: true,
            show_submit_booking_error: false,
            validation: "",
          });
        } else if (body !== null && body["statusCode"] == 400) {
          setState({
            ...state,
            show_submit_booking_success: false,
            show_submit_booking_error: true,
          });
        }
      })
      .catch((err) => console.log(err));
  };

  // handle form validation
  const validateTime = (time, beforeTime, format) => {
    const TIME = moment(time);
    return (
      TIME.isValid() && // valid date
      time === TIME.format(format) && // valid format
      TIME > moment(beforeTime)
    ); // valid time (after specified time)
  };

  const validateEmail = (email) => {
    var re = /\S+@\S+\.\S+/; // checks if email is in format: anyString@anyString.anyString
    return re.test(email);
  };

  const validateForm = () => {
    const { form } = state;
    const formValid = {
      // destination_id: Number.isInteger(Number(state.destination_id)),
      // hotel_id: Number.isInteger(Number(state.hotel_id)),
      // hotel_room_id: Number.isInteger(Number(state.hotel_id)),
      // price: typeof state.price == "number",
      //start_date: validateTime(form.start_date, Date.now(), "YYYY-MM-DD"),
      //end_date: validateTime(form.end_date, form.start_date, "YYYY-MM-DD"),
      card_number: state.payments.card_number !== "",
      card_name: state.payments.card_name !== "",
      expiry_month: state.payments.expiry_month !== "",
      expiry_year: state.payments.expiry_year !== "",
      cvv_cvc: state.payments.cvv_cvc !== "",
      cust_firstname:
        state.form.cust_firstname !== "" &&
        ("" + state.cust_firstname).length < 10,
      cust_lastname:
        state.form.cust_lastname !== "" &&
        ("" + state.cust_lastname).length < 10,
      cust_email:
        state.form.cust_email !== "" && validateEmail(form.cust_email),
    };
    console.log(formValid);
    if (Object.values(formValid).every((item) => item === true)) return true;
    console.log("Form validation error");
    const validationDetails = {
      // destination_id: "destination_id not in list",
      // hotel_id: "hotel_id is invalid",
      // hotel_room_id: "hotel_room_id is invalid",
      // price: "price is invalid",
      // start_date:
      //   "invalid time: wrong format OR start date before current date",
      // end_date: "invalid time: wrong format OR end date before start date",
      card_number: "Please fill in any missing payment details",
      card_name: "Please fill in any missing payment details",
      expiry_month: "Please fill in any missing payment details",
      expiry_year: "Please fill in any missing payment details",
      cvv_cvc: "Please fill in any missing payment details",
      cust_firstname: "cust_firstname is too long (more than 10 characters)",
      cust_lastname: "cust_lastname is too long (more than 10 characters)",
      cust_email: "email is in an invalid format",
    };
    for (let i = 0; i < Object.keys(formValid).length; i++) {
      const ele = Object.keys(formValid)[i];
      if (formValid[ele]) continue;
      setState({
        ...state,
        validation: validationDetails[ele],
      });
      break;
    }
    return false;
  };
  // form: {
  //   destination_id: "",
  //   hotel_id: "", // from previous page
  //   hotel_room_id: "",
  //   price: "", // from backend
  //   start_date: "", // from previous page
  //   end_date: "", // from previous page
  //   cust_firstname: "", //? from this page
  //   cust_lastname: "", //? from this page
  //   cust_email: "" //? from this page

  // },


  return (
    <>
      {/* form validation */}
      <div>
        {state.validation === "" ? (
          <></>
        ) : (
          <>
            <div className="alert alert-danger" role="alert">
              {state.validation}
            </div>
          </>
        )}
      </div>

      {/* booking confirmation */}
      <div>
        {state.show_submit_booking_success ? (
          <>
            <div className="alert alert-success" role="alert">
              Your booking has been confirmed!
            </div>
          </>
        ) : (
          <></>
        )}
      </div>
      {/* booking failure */}
      <div>
        {state.show_submit_booking_error ? (
          <>
            <div className="alert alert-danger" role="alert">
              There is a problem. Your booking has failed to submit.
            </div>
          </>
        ) : (
          <></>
        )}
      </div>
      <div className={classes.root}>
        {/* name and email */}
        <div>
          <div className="mb-3">
            <label htmlFor="email" className="form-label">
              Email
            </label>
            <input
              type="email"
              className="form-control"
              id="email"
              onChange={handleForm("cust_email")}
            />
          </div>

          {/* salutation dropdown */}
          <div className="mb-3">
            <label htmlFor="cust_salutation" className="form-label">
              Salutation
            </label>
            <select className="form-select" aria-label="Default select example">
              {cust_salutations.map((item) => {
                return <option key={item}>{item}</option>;
              })}
            </select>
          </div>

          <div className="mb-3">
            <label htmlFor="cust_firstname" className="form-label">
              First Name
            </label>
            <input
              type="text"
              className="form-control"
              id="cust_firstname"
              onChange={handleForm("cust_firstname")}
            />
          </div>
          <div className="mb-3">
            <label htmlFor="cust_lastName" className="form-label">
              Last Name
            </label>
            <input
              type="text"
              className="form-control"
              id="cust_lastName"
              onChange={handleForm("cust_lastname")}
            />
          </div>
        </div>

        {/* payments */}
        <div>
          <div className="mb-3">
            <label htmlFor="cardNumber" className="form-label">
              Card Number
            </label>
            <input
              type="text"
              className="form-control"
              id="cardNumber"
              onChange={handlePayments("card_number")}
            />
          </div>

          {/* card name */}
          <div className="mb-3">
            <label htmlFor="cardName" className="form-label">
              Name on Card
            </label>
            <input
              type="text"
              className="form-control"
              id="cardName"
              onChange={handlePayments("card_name")}
            />
          </div>

          {/* card expiry */}
          <label htmlFor="cardExpiryMonth" className="form-label"></label>
          <div className="input-group">
            <span className="input-group-text">Card Expiry Date</span>
            <input
              id="cardExpiryMonth"
              type="text"
              aria-label="cardExpiryMonth"
              placeholder="Month (MM)"
              className="form-control"
              onChange={handlePayments("expiry_month")}
            />
            <input
              id="cardExpiryYear"
              type="text"
              aria-label="cardExpiryYear"
              placeholder="Year (YY)"
              className="form-control"
              onChange={handlePayments("expiry_year")}
            />
          </div>

          {/* cvv/cvc */}
          <div className="mb-3">
            <label htmlFor="cvv_cvc" className="form-label">
              CVV/CVC
              {/* TODO: add validation for 3 numbers */}
            </label>
            <input
              type="password"
              className="form-control"
              id="cvv_cvc"
              onChange={handlePayments("cvv_cvc")}
            />
          </div>
        </div>
      </div>
      {/* submit button */}
      <div className="d-grid gap-2 d-md-flex justify-content-md-end">
        <button
          className="btn btn-primary"
          type="button"
          onClick={() => {
            console.log(state);
            if (!validateForm()) return;
            //submitBooking(state.form);
            submitBooking()
            //console.log(state);
          }}
        >
          Submit
        </button>
      </div>
    </>
  );
};

export default Page;

const bookingState = {
  form: {
    destination_id: "",
    hotel_id: "", // from previous page
    hotel_room_id: "",
    price: "test", // from backend
    start_date: "test", // from previous page
    end_date: "test", // from previous page
    cust_firstname: "", //? from this page
    cust_lastname: "", //? from this page
    cust_email: "", //? from this page
  },
  payments: {
    card_number: "",
    card_name: "",
    expiry_month: "",
    expiry_year: "",
    cvv_cvc: "",
  },
  validation: "",

  show_submit_booking_success: false,
  show_submit_booking_error: false,
};
