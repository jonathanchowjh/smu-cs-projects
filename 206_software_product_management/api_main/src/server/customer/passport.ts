import passport from 'passport';

import { CustomerDB } from '@mongo/models';

passport.serializeUser((user: any, callback: any) => {
  callback(null, user.id);
});

passport.deserializeUser(async (id: Object, callback: any) => {
  try {
    const user = await CustomerDB.findById(id);
    if (user) return callback(null, user);
    return callback(null, false);
  } catch {
    return callback(null, false);
  }
});

export default passport;
