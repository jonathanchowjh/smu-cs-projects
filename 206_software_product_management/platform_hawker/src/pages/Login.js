import React, { useState } from 'react'
import axios from 'axios'


const Login = (props) => {
  const [form, setForm] = useState({ uen: "", password: "" })
  const handleLogin = () => {
    console.log(form)
    login(form)
  }

  // Method for login
  const login = async (form) => {
    const res = await axios({
      method: 'post',
      url: 'https://jiak-api.vitaverify.me/api/v1/stall/auth/login',
      data: form,
      withCredentials: true
    })
      .then(function (res) {
        console.log(res);
        window.location.pathname = "/orders";
      })
      .catch(function (error) {
        console.log(error);
      })

    return res  
  }

  return (
    <div className="container border p-3 mt-5">
      <p className="elements-login" style={{ fontWeight: 800, fontSize: '24px', textAlign: 'left'}}> Welcome back</p>
      <p className="elements-login" style={{fontWeight: 600, fontSize: '16px', fontStyle: 'italic', textAlign: 'left'}}> Sign in to continue</p>
      <div className="mb-3" align="left">
        <label for="formGroupExampleInput" className="form-label" style={{ color: "#EE693F", fontWeight: 600, fontSize: '16px', textAlign: 'left'}}>UEN</label>
        <input type="text" className="form-control" id="formGroupExampleInput" placeholder="eg. 201012345X"
          onChange={(e)=>setForm({ ...form, uen: e.target.value })}
          value={form.uen}
        />
      </div>
      <div className="mb-3" align="left">
        <label for="formGroupExampleInput2" className="form-label" style={{ color: "#EE693F", fontWeight: 600, fontSize: '16px', textAlign: 'left'}}>Password</label>
        <input type="text" className="form-control" id="formGroupExampleInput2" placeholder="eg. 2u2jbd%Ts./2"
          onChange={(e)=>setForm({ ...form, password: e.target.value })}
          value={form.password}
        />
      </div>
      <button type="button" className="btn btn-primary btn-lg btn-login" onClick={handleLogin}>Log in</button>
    </div>
  )
}


export default Login;