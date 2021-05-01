import hawkers from './states/hawker'
import stalls from './states/stalls'
import menus from './states/menus'
export default {
  backend: true,
  user: {
    date: 0,
    phone: "",
    email: "",
    password: ""
  },
  map: {
    name: "",
    image: "",
    coordinates: [
      {
        uen: "",
        mapId: "",
        startX: 0,
        startY: 0,
        endX: 0,
        endY: 0
      }
    ],
    tables: [
      {
        mapId: "",
        number: 0,
        coordX: 0,
        coordY: 0
      }
    ]
  },
  orders: null,
  cart: [],
  menus: [],
  stalls: [],
  hawkers: []
}