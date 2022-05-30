import React from 'react';

const HotelCard = (props) => {
  const { title, text, textTwo, textThree, image_link, onClick, loading } = props;
  if (loading) {
    return (
      <div className="m-2 card d-flex flex-row" style={{ height: "150px", maxHeight: "150px" }} aria-hidden="true">
        <img
          src="/placeholder.jpg"
          className="card-img-top"
          alt="..."
          style={{ height: "150px", width: "150px", objectFit: "cover" }}
          loading="lazy"
        />
        <div className="card-body">
          <h5 className="card-title placeholder-glow">
            <span className="placeholder col-6"></span>
          </h5>
          <p className="card-text placeholder-glow">
            <span className="placeholder col-7"></span>
            <span className="placeholder col-4"></span>
            <span className="placeholder col-4"></span>
            <span className="placeholder col-6"></span>
            <span className="placeholder col-8"></span>
          </p>
        </div>
      </div>
    )
  }
  return (
    <div className="m-2 card d-flex flex-row" style={{ height: "150px", maxHeight: "150px" }}>
      <img
        className="mw-100"
        src={image_link}
        onError={({ currentTarget }) => {
          currentTarget.onerror = null; // prevents looping
          currentTarget.src="/placeholder.jpg";
        }}
        alt="Card image cap"
        style={{ height: "150px", width: "150px", objectFit: "cover" }}
        loading="lazy"
      />
      <div className="card-body">
        <h5 className="card-title">{title}</h5>
        <p className="card-text" style={{ margin: 0 }}>{text}</p>
        <div className="d-flex justify-content-between">
          <div>
            <p className="card-text" style={{ margin: 0 }}>{textTwo}</p>
            <p className="card-text" style={{ margin: 0 }}>{textThree}</p>
          </div>
          <button
            className="btn btn-primary"
            onClick={onClick}
          >
            Book
          </button>
        </div>
      </div>
    </div>
  )
}

export default HotelCard;
