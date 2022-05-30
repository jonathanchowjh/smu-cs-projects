import React, { useState } from 'react'
import moment from 'moment';
import { useNavigate } from "react-router-dom";

import InputSelect from '../components/InputSelect';
import InputDataList from '../components/InputDataList';
import InputDateRange from '../components/InputDateRange';

// To Do - integrate dest search, WAF, Hotel lazy loading
const Index = () => {
  const [state, setState] = useState(defaultState);
  const navigate = useNavigate();

  const handleForm = (attrName) => (e) => {
    const val = e.target ? e.target.value : e;
    setState({
      ...state,
      form: {
        ...state.form,
        [attrName]: val
      }
    });
  };

  const handleSearch = () => (e) => {
    if (e.target.value.length < 2) return;
    const url = `https://zrv1f7pyq2.execute-api.ap-southeast-1.amazonaws.com/test?q=${e.target.value}&suggester=fuzzy`;
    // Fetch Value
    fetch(url,
      {
        method: 'GET',
        headers: { accept: 'application/json' }
      }
    ).then((response) => {
      return response.json();
    }).then((res) => {
      console.log(res);
      if (res && res.suggest && res.suggest.suggestions && res.suggest.suggestions.map)
      setState({
        ...state,
        destinations: res.suggest.suggestions.map((ele) => ({
          name: ele.suggestion ? ele.suggestion : "",
          key: ele.id ? ele.id : "",
          country_code: ele.country_code ? ele.country_code : ""
        }))
      })
    }).catch((err) => console.log(err));
  };

  const handleDateRange = (dateRange) => {
    setState({
      ...state,
      form: {
        ...state.form,
        checkin: moment(dateRange[0]).format('YYYY-MM-DD'),
        checkout: moment(dateRange[1]).format('YYYY-MM-DD')
      },
      dateRange: dateRange
    });
  };

  const validateTime = (time, beforeTime, format) => {
    const TIME = moment(time);
    return TIME.isValid() // valid date
      && time === TIME.format(format) // valid format
      && TIME > moment(beforeTime); // valid time (after specified time)
  }

  const validateForm = () => {
    const { form } = state;
    const formValid = {
      destination_id: state.destinations.some((ele) => ele.key === form.destination_id),
      country_code: state.destinations.some((ele) => ele.country_code === form.country_code),
      checkin: validateTime(form.checkin, Date.now(), 'YYYY-MM-DD'),
      checkout: validateTime(form.checkout, form.checkin, 'YYYY-MM-DD'),
      lang: state.language.some((ele) => ele.key === form.lang),
      currency: state.currency.some((ele) => ele.key === form.currency),
      guests: state.guests.some((ele) => ele.key === form.guests),
      partner_id: state.form.partner_id === "1"
    };
    if (Object.values(formValid).every(item => item === true)) return true;
    const validationDetails = {
      destination_id: "destination_id not in list",
      country_code: "country_code not in list",
      checkin: "invalid time: wrong format OR start date before current date",
      checkout: "invalid time: wrong format OR end date before start date",
      lang: "language not in list",
      currency: "currency not in list",
      guests: "guests not in list",
      partner_id: "partner_id not 1"
    }
    for (let i = 0; i < Object.keys(formValid).length; i++) {
      const ele = Object.keys(formValid)[i];
      if (formValid[ele]) continue;
      setState({
        ...state,
        validation: validationDetails[ele]
      })
      break;
    }
    return false;
  }

  return (
    <div style={{ width: "70%", margin: "0 auto" }}>
      {/* title of page */}
      <h2 className="mx-2 my-5">
        HOTEL SCANNER
      </h2>

      {
        state.validation === "" ? <></> : (
          <div className="alert alert-danger m-2" role="alert">
            {state.validation}
          </div>
        )
      }

      {/* destination_id */}
      <InputDataList
        name="Destination"
        list={state.destinations}
        onChange={handleSearch()}
        onSelect={(e) => setState({
          ...state,
          form: {
            ...state.form,
            destination_id: e,
            country_code: ""
          }
        })}
      />

      <div className="d-flex">
        <div className="d-flex justify-content-between">
          {/* Language */}
          <InputSelect
            name="language"
            list={state.language}
            onChange={handleForm("lang")}
          />

          {/* currency */}
          <InputSelect
            name="currency"
            list={state.currency}
            onChange={handleForm("currency")}
          />

          {/* guests */}
          <InputSelect
            name="guests"
            list={state.guests}
            onChange={handleForm("guests")}
            className=""
          />
        </div>
      </div>

      {/* checkin and checkout */}
      <InputDateRange
        value={state.dateRange}
        setValue={handleDateRange}
      />

      <div className="d-grid gap-2 d-md-flex justify-content-md-end">
        <button
          className="btn btn-primary"
          type="button"
          onClick={() => {
            console.log(state.form);
            if (!validateForm()) return;
            navigate(
              `/hotel?destination_id=${state.form.destination_id}`
              + `&checkin=${state.form.checkin}`
              + `&checkout=${state.form.checkout}`
              + `&lang=${state.form.lang}`
              + `&currency=${state.form.currency}`
              + `&country_code=${state.form.country_code}`
              + `&guests=${state.form.guests}`
              + `&partner_id=${state.form.partner_id}`
            );
          }}
        >
          Submit
        </button>
      </div>
    </div>
  )
}
export default Index;

const defaultState = {
  form: {
    destination_id: "",
    country_code: "",
    checkin: moment(Date.now()).format('YYYY-MM-DD'),
    checkout: moment(Date.now()).format('YYYY-MM-DD'),
    lang: "en_US",
    currency: "SGD",
    guests: "1",
    partner_id: "1"
  },
  validation: "",
  dateRange: [null, null],
  destinations: [
    {
      name: "Singapore",
      key: "SG",
      country_code: "SG",
    },
    {
      name: "Malaysia",
      key: "MY",
      country_code: "MY",
    }
  ],
  language: [
    {
      name: "en_US",
      key: "en_US"
    },
  ],
  currency: [
    {
      name: "SGD",
      key: "SGD"
    },
  ],
  guests: [
    {
      name: "1",
      key: "1"
    },
    {
      name: "2",
      key: "2"
    },
    {
      name: "3",
      key: "3"
    },
    {
      name: "4",
      key: "4"
    },
    {
      name: "5",
      key: "5"
    }
  ],
}
