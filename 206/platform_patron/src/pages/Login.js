import React, { useState } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { login } from '../redux/reducers/main'


const Login = (props) => {
  const [form, setForm] = useState({ phone: "", password: "" })
  const handleLogin = () => {
    console.log(form)
    props.login(form)
  }
  return (
    <div className="container border p-3 mt-5">
      <p className="elements-login" style={{ fontWeight: 800, fontSize: '24px', textAlign: 'left'}}> Welcome back</p>
      <p className="elements-login" style={{fontWeight: 600, fontSize: '16px', fontStyle: 'italic', textAlign: 'left'}}> Sign in to continue</p>
      <div className="mb-3" align="left">
        <label for="formGroupExampleInput" className="form-label" style={{ color: "#EE693F", fontWeight: 600, fontSize: '16px', textAlign: 'left'}}>Mobile number</label>
        <input type="text" className="form-control" id="formGroupExampleInput" placeholder="eg. 92836172"
          onChange={(e)=>setForm({ ...form, phone: e.target.value })}
          value={form.phone}
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

const mapStateToProps = redux => ({ redux })
const mapDispatchToProps = dispatch => {
  return bindActionCreators({ login }, dispatch)
}


export default connect(mapStateToProps, mapDispatchToProps)(Login);