import React from "react";

const InputDataList = (props) => {
  const { onChange, onSelect, list, name, className } = props;
  const [focused, setFocused] = React.useState(false);
  const [value, setValue] = React.useState("");

  return (
    <div
      className={`m-2 ${className}`}
      style={{ border: "1px solid #bdbdbd", borderRadius: "10px", padding: "10px" }}
    >
      <label htmlFor="exampleDataList" className="form-label">{name}</label>
      <input
        className="form-control" list="datalistOptions" id="exampleDataList" placeholder="Type to search..."
        onChange={(e) => {
          setValue(e.target.value)
          onChange(e)
        }}
        value={value}
        autoComplete="off"
        onFocus={() => setFocused(true)}
        onBlur={() => setTimeout(() => setFocused(false), 300)}
      />
      <div id="datalistOptions">
        {
          focused && list.map((ele) => {
            return <div
                value={ele.name}
                key={ele.key}
                onClick={() => {
                  setValue(ele.name)
                  onSelect(ele.key)
                }}
              >
                {ele.name}
              </div>
          })
        }
      </div>
    </div>
  )
}

export default InputDataList;