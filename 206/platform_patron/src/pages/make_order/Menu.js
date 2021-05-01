import React, { useEffect, useState } from 'react';
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { navigate, updateCart, getMenu, back } from '../../redux/reducers/main'
import { findInList, findByKeysInList, isFalse } from '../../common/functions'

const Menu = (props) => {
  const [menu, setMenu] = useState(0);
  const { menus, cart } = props.redux.main;

  const [form, setForm] = useState({
    specialOrder: "",
    amount: 1
  });

  useEffect(() => {
    let num = null;
    const { _id, uen } = props.redux.router.location.query;
    num = findByKeysInList(menus, "_id", _id, "uen", uen)
    if (isFalse(num)) {
      // get hawker
      props.getMenu({ uen, id: _id })
    }
    setMenu(num);

    num = findInList(cart, "menuid", _id);
    if (!isFalse(num)) setForm({...form, amount: cart[num].quantity})
  }, [menus])
  return (
    <div>
      {
        menus[menu] && (
          <div className="dish-card m-3 text-start">
            <img
              alt=""
              src={menus[menu].image}
              style={{
                height: "10em",
                width: "10em",
                backgroundRepeat: "no-repeat",
                backgroundSize: "auto 100%",
                backgroundImage: `url(${menus[menu].image}), url(https://martialartsplusinc.com/wp-content/uploads/2017/04/default-image-620x600.jpg)`
              }}
            />
            <div className="card-body">
              <div className="card-title em-15 dish-name-price" style={{height: "30px"}}>{menus[menu].name} <span class="badge bg-secondary">{form.amount}</span></div>
              <div className="dish-name-price" style={{height: "40px"}}>{`$${menus[menu].price}`}</div>
              <div className="dish-name-price" style={{color: "#7F7F7F", fontWeight: 400}}>{menus[menu].description}</div>

              <div className="form-floating my-3" style={{color: "#7F7F7F"}}>
                <textarea
                  className="form-control"
                  placeholder="Leave a comment here"
                  id="floatingTextarea2"
                  style={{height: "100px"}}
                  value={form.specialOrder}
                  onChange={e => setForm({...form, specialOrder: e.target.value})}
                />
                <label for="floatingTextarea2" >Special Instructions (optional)</label>
              </div>
              <div className="btn-group my-3 d-block" role="group" aria-label="Basic example">
                <button type="button" className="btn btn-primary dish-btn" onClick={() => setForm({...form, amount: form.amount > 1 ? form.amount - 1 : form.amount})}>-</button>
                <button type="button" className="btn btn-primary dish-btn" onClick={() => setForm({...form, amount: 1})}>Reset</button>
                <button type="button" className="btn btn-primary dish-btn" onClick={() => setForm({...form, amount: form.amount + 1})}>+</button>
              </div>
              <div
                onClick={() => {
                  const { localStorage } = window;
                  let storageCart = localStorage.getItem('cs206_cart')
                  storageCart = storageCart == null ? [] : JSON.parse(storageCart)
                  storageCart = storageCart.filter((listItem) => listItem.menuid != menus[menu]._id)
                  localStorage.setItem('cs206_cart',
                    JSON.stringify([ ...storageCart, {
                      menuid: menus[menu]._id,
                      uen: menus[menu].uen,
                      quantity: form.amount,
                      specialOrder: form.specialOrder
                    }])
                  )
                  props.updateCart({
                    type: "EDIT_CART",
                    data: {
                      menuid: menus[menu]._id,
                      uen: menus[menu].uen,
                      quantity: form.amount,
                      specialOrder: form.specialOrder
                    }
                  })
                  props.back()
                }}
                className="btn btn-primary dish-btn"
              >
                Add to Cart
              </div>
            </div>
          </div>
        )
      }
    </div>
  )
}

const mapStateToProps = redux => ({ redux })
const mapDispatchToProps = dispatch => {
  return bindActionCreators({ navigate, updateCart, getMenu, back }, dispatch)
}


export default connect(mapStateToProps, mapDispatchToProps)(Menu);
