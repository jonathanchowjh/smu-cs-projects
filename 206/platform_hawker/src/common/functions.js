export const findInList = (list, key, value) => {
  for (let i = 0; i < list.length; i++) {
    if (list[i][key] === value) return i;
  }
  return false;
}
export const getParam = (str, pre, post="") => {
  str = str.replace(pre, "");
  str = str.replace(post, "");
  return str;
}
export const isFalse = (variable) => {
  if (typeof variable != "boolean") return false;
  return !variable
}
export const openingHours = (operatingHours) => {
  const date = new Date(); // current date
  const day = date.getDay()
  console.log(day)
  if (day === 1) return operatingHours.monday
  if (day === 2) return operatingHours.tuesday
  if (day === 3) return operatingHours.wednesday
  if (day === 4) return operatingHours.thursday
  if (day === 5) return operatingHours.friday
  if (day === 6) return operatingHours.saturday
  if (day === 0) return operatingHours.sunday
  return null
}
export const isOpen = (openingHours) => {
  const date = new Date()
  const hour = date.getHours();
  // if a stall is close for the day
  if (openingHours === null) return false;
  if (openingHours.open < hour && openingHours.close > hour) return true;
  
  return false;
}
