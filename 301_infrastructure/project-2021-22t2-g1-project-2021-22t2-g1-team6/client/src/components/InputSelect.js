import React from "react";

const InputSelect = (props) => {
  const { selectOne, onChange, list, name, className } = props;
  return (
    <div className={`form-floating m-2 ${className}`}>
      <select
        className="form-select px-5"
        onChange={onChange}
        id="floatingSelect" aria-label="Floating label select example"
      >
        {
          selectOne ? <option selected>select one</option> : <></>
        }
        {
          list.map((ele) => {
            return <option value={ele.name} key={ele.name}>{ele.name}</option>
          })
        }
      </select>
      <label htmlFor="floatingSelect">{name}</label>
    </div>
  )
}

export default InputSelect;