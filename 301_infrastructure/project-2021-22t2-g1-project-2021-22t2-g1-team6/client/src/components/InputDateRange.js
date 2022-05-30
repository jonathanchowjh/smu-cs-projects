import * as React from 'react';
import TextField from '@mui/material/TextField';
import AdapterDateFns from '@mui/lab/AdapterDateFns';
import LocalizationProvider from '@mui/lab/LocalizationProvider';
import Box from '@mui/material/Box';
import Stack from '@mui/material/Stack';
// import MobileDateRangePicker from '@mui/lab/MobileDateRangePicker';
import DesktopDateRangePicker from '@mui/lab/DesktopDateRangePicker';

const InputDateRange = (props) => {
  const { value, setValue } = props;
  // const [dimensions, setDimensions] = React.useState({ width: window.innerWidth, height: window.innerHeight });
  // const updateDimensions = () => {
  //   console.log({ width: window.innerWidth, height: window.innerHeight })
  //   setDimensions({ width: window.innerWidth, height: window.innerHeight })
  // };
  // React.useEffect(() => {
  //   console.log(dimensions)
  //   window.addEventListener("resize", updateDimensions);
  //   return () => window.removeEventListener("resize", updateDimensions);
  // }, [])

  return (
    <div className='m-2'>
      <LocalizationProvider dateAdapter={AdapterDateFns}>
        <Stack spacing={3}>
          <DesktopDateRangePicker
            startText="Start Date"
            value={value}
            onChange={(newValue) => {
              setValue(newValue);
            }}
            renderInput={(startProps, endProps) => (
              <React.Fragment>
                <TextField {...startProps} />
                <Box sx={{ mx: 2 }}> to </Box>
                <TextField {...endProps} />
              </React.Fragment>
            )}
          />          
        </Stack>
      </LocalizationProvider>
    </div>
  );
}

export default InputDateRange;

// <MobileDateRangePicker
//   startText="Mobile start"
//   value={value}
//   onChange={(newValue) => {
//     setValue(newValue);
//   }}
//   renderInput={(startProps, endProps) => (
//     <React.Fragment>
//       <TextField {...startProps} />
//       <Box sx={{ mx: 2 }}> to </Box>
//       <TextField {...endProps} />
//     </React.Fragment>
//   )}
// />