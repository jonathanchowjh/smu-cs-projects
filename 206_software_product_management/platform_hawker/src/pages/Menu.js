import React, { useState, useEffect } from 'react'
import axios from 'axios'
import MenuCard from "../components/MenuCard"
import AddMenuItem from "../components/AddMenuItem"


const Menu = (props) => {
  const [menu, setMenu] = useState([])
  const [item, setItem] = useState({name: "", price: 0, description: "", image: ""})
  const [reload, setReload] = useState(0)

  useEffect(() => {
    axios({
      method: 'get',
      url: 'https://jiak-api.vitaverify.me/api/v1/stall/menu',
      withCredentials: true
    })
      .then(function (res) {
        console.log(res)
        setMenu(res.data)
      })
      .catch(function (error) {
        console.log(error)
      })
  }, [reload])

  const addItem = () => {
    axios({
      method: 'post',
      url: 'https://jiak-api.vitaverify.me/api/v1/stall/menu',
      data: item,
      withCredentials: true
    })
      .then(function (res) {
        console.log(res)
        setReload(reload + 1)
      })
      .catch(function (error) {
        console.log(error)
      })
  }

  return (
    <div>
      <div className="headers"><h3>Menu Items</h3></div>
      <div style={{maxHeight:"40vh", overflow: "scroll"}}>
        {
          menu && menu.map((ele) => {
            console.log(ele)
            return (
              <MenuCard 
                image={ele.image}
                title={ele.name}
                line1={ele.description}
                line2={`Price: S$${ele.price}`}
                l2Class="text-success"
              />
            )
          })
        }
      </div>
      <AddMenuItem
        item={item}
        setItem={setItem}
        addItem={addItem}
      />
    </div>
  )
}

export default Menu
