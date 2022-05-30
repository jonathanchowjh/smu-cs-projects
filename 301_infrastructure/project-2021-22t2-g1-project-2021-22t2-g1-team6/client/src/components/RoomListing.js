import React from "react";

const RoomListing = (props) => {
  // const { title, text, textTwo, textThree, image_link, onClick, loading } = props;
  //console.log(props.room_data);
  if (props.loading) {
    return (
      <div
        className="m-2 card d-flex flex-row"
        style={{ height: "150px", maxHeight: "150px" }}
        aria-hidden="true"
      >
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
    );
  }
  return (
    <div
      className="m-2 card d-flex flex-row"
      style={{ height: "150px", maxHeight: "150px" }}
    >
      {props.room_data.image.url === "" ? (
        <img
          className="mw-100"
          src="/placeholder.jpg"
          alt="Hotel Room Image"
          style={{ height: "150px", width: "150px", objectFit: "cover" }}
          loading="lazy"
        />
      ) : (
        <img
          className="mw-100"
          src={props.room_data.image.url}
          onError={({ currentTarget }) => {
            currentTarget.onerror = null; // prevents looping
            currentTarget.src = "/placeholder.jpg";
          }}
          alt="Hotel Room Image"
          style={{ height: "150px", width: "150px", objectFit: "cover" }}
          loading="lazy"
        />
      )}

      <div className="card-body">
        <h5 className="card-title">
          {props.room_data.roomNormalizedDescription}
        </h5>
        <p
          className="card-text text-danger fw-bold"
          style={{ margin: 0, fontSize: "20px" }}
        >
          Price: ${props.room_data.price}
        </p>
        <p
          className="card-text text-success fw-bold"
          style={{ margin: 0, fontSize: "20px" }}
        >
          Points: {props.room_data.points}
        </p>
        <div className="d-flex justify-content-between">
          <div>
            <p className="card-text" style={{ margin: 0 }}>
              {props.room_data.amenities.length === 0 ? (
                <></>
              ) : (
                <>
                  Amenities:{" "}
                  {props.room_data.amenities.slice(0, 4).map((item, i) => (
                    <React.Fragment key={i}>{item}, </React.Fragment>
                  ))}{" "}
                  ...
                </>
              )}
            </p>
          </div>
          <button className="btn btn-primary" onClick={props.onClick}>
            Reserve
          </button>
        </div>
      </div>
    </div>
  );
};

export default RoomListing;
