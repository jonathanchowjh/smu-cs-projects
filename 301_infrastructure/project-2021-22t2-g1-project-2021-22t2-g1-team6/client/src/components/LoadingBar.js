import React from "react";

const LoadingBar = () => {
  return (
    <div>
      <div class="spinner-grow text-primary m-1" role="status">
        <span class="sr-only"></span>
      </div>
      <div class="spinner-grow text-secondary m-1" role="status">
        <span class="sr-only"></span>
      </div>
      <div class="spinner-grow text-success m-1" role="status">
        <span class="sr-only"></span>
      </div>
      <div class="spinner-grow text-danger m-1" role="status">
        <span class="sr-only"></span>
      </div>
      <div class="spinner-grow text-warning m-1" role="status">
        <span class="sr-only"></span>
      </div>
      <div class="spinner-grow text-info m-1" role="status">
        <span class="sr-only"></span>
      </div>
      <div class="spinner-grow text-light m-1" role="status">
        <span class="sr-only"></span>
      </div>
      <div class="spinner-grow text-dark m-1" role="status">
        <span class="sr-only"></span>
      </div>
    </div>
  )
}

export default LoadingBar;