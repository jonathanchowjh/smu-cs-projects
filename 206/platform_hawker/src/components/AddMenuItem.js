import React from 'react'


const AddMenuItem = (props) => {
  const {item, setItem, addItem} = props
  const {name, price, description} = item
  

  return (
    <div className='card-block text-start p-3'>
      <p>Add new menu item</p>
      <form className='add-menu-item'>
        <div className='form-control'>
          <label>Dish name</label>
          <br></br>
          <input 
            type="text"
            placeholder='Add name'
            value={name}
            onChange={(e) => setItem({ ...item, name: e.target.value })}
          />
        </div>

        <div className='form-control'>
          <label>Price</label>
          <br></br>
          <input 
            type="number" step="0.01"
            placeholder='Add price'
            value={price}
            onChange={(e) => setItem({ ...item, price: e.target.value })}
          />
        </div>

        <div className='form-control'>
          <label>Description</label>
          <br></br>
          <input 
            type="text" 
            placeholder='Add description'
            value={description}
            onChange={(e) => setItem({ ...item, description: e.target.value })}/>
        </div>
      </form>
      <button className="btn btn-primary btn-sm mr-2 px-4" onClick={() => addItem()}>Save item</button>
    </div>
  )
}

export default AddMenuItem
