const logout = async (req: any, res: any) => {
  const isHTTPS = process.env.NODE_ENV !== 'local';
  res.clearCookie('customer-token', {
    domain: isHTTPS ? '.vitaverify.me' : 'localhost',
    path: '/',
    httpOnly: true,
    maxAge: 1000 * 3600 * 24 * 30,
    sameSite: isHTTPS ? 'none' : true,
    secure: isHTTPS,
  });
  return res.status(200).json({ data: true });
};

export default logout;
