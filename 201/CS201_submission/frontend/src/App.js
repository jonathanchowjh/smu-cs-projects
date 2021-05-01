import React, { useState } from 'react'
import Loader from 'react-loader-spinner'
import Modal from 'react-modal';
import './App.css';
// import qs from 'qs'

/* eslint-disable no-console */

const customStyles = {
  content : {
    top                   : '50%',
    left                  : '50%',
    right                 : 'auto',
    bottom                : 'auto',
    marginRight           : '-50%',
    transform             : 'translate(-50%, -50%)'
  }
};

const Page = () => {
  const [word, setWord] = useState('');
  const [num, setNum] = useState(5);
  const [sentences, setSentences] = useState([]);
  
  // MODAL SPINNER
  const [modalIsOpen, setIsOpen] = useState(false);
  const openModal = () => setIsOpen(true);
  const closeModal = () => setIsOpen(false);

  const makeRequest = () => {
    const fetchParams = {
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      method: 'POST',
      mode: 'cors',
      body: JSON.stringify({first_word: word, max_length: num})
    };
    fetch("http://localhost:8080/api/model/predict", fetchParams)
      .then(res => res.json())
      .then((res) => {
        closeModal()
        setSentences([
          {
            joke: res.joke,
            first_word: word,
            max_length: num
          },
          ...sentences
        ])
      })
      .catch((err) => {
        closeModal();
        console.log(err);
      })
  }

  const handleSubmit = () => {
    if (modalIsOpen) return;
    openModal();
    makeRequest();
  }
  return (
    <div className="page">
      <Modal
        isOpen={modalIsOpen}
        // onRequestClose={closeModal}
        style={customStyles}
        contentLabel="Example Modal"
      >
        <br/>
        <div>
          <div className="bigger">Loading Joke</div>
          <div>Might take some time if words needs to be compiled</div>
        </div>
        <br/><br/>
        <Loader
          type="Grid"
          color="#41487d"
          height={80}
          width={80}
          //  timeout={3000} //3 secs
        /><br/>
      </Modal>
      <div className="page-header">
        <div>Generating Sentences (Jokes)</div>
      </div>
      <div className="page-input">
        <div className="page-input-div">
          <div>
            <div>Enter a first word of Joke :</div>
            <div>Leave blank auto generate first word</div>
            <input
              type="text"
              onChange={(e) => { setWord(e.target.value) }}
              value={word}
              placeholder="Starting Word..."
            />
          </div>
          <div>
            <div>Enter max length of Joke :</div>
            <input
              type="number"
              onChange={(e) => { setNum(e.target.value) }}
              value={num}
            />
          </div>
          <button onClick={handleSubmit}>GENERATE a Joke!</button>
        </div>
      </div>
      <div className="page-list">
        <div className="page-list-header">Sentences:</div>
        <div className="page-list-map">
          {sentences.map((sentence) => {
            return (
              <div className="page-list-sentence">
                &quot;
                {`${sentence.first_word} ${sentence.joke}`}
                &quot;
              </div>
            )
          })}
        </div>
      </div>
    </div>
  )
}

export default Page
