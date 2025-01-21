import React, { JSX } from 'react';
import { AppBar, Box, Button, Toolbar, Typography, useMediaQuery, useTheme } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import Loader from './Loader';
import SwitchLanguage from './SwitchLanguage';

type Props = {
    children: JSX.Element;
};

const navItems = [
    { label: 'Boutiques', path: '/' },
    { label: 'Produits', path: '/product' },
    { label: 'Catégories', path: '/category' },
];

const Layout = ({ children }: Props) => {
    const navigate = useNavigate();
    const isMobile = useMediaQuery((theme: any) => theme.breakpoints.down('sm'));
    const [drawerOpen, setDrawerOpen] = React.useState(false);
    const theme = useTheme();
    const toggleDrawer = () => {
        setDrawerOpen(!drawerOpen);
    };
    const appBarHeight = isMobile ? theme.spacing(7) : theme.spacing(8);

    return (
        <div>
            <AppBar component="nav">
                <Toolbar className="header">
                    <Typography variant="h6" onClick={() => navigate('/')} sx={{ cursor: 'pointer', flexGrow: '1'}}>
                        Gestion de boutiques
                    </Typography>
                    {isMobile ? (
                        <>
                            <Button sx={{ color: '#fff' }} onClick={toggleDrawer}>
                                Menu
                            </Button>
                            <Drawer
                                anchor="right"
                                open={drawerOpen}
                                onClose={toggleDrawer}
                            >
                                <Box sx={{ width: 250 }}>
                                    {navItems.map((item) => (
                                        <Button
                                            key={item.label}
                                            sx={{ color: '#000', width: '100%' }}
                                            onClick={() => {
                                                navigate(item.path);
                                                setDrawerOpen(false);
                                            }}
                                        >
                                            {item.label}
                                        </Button>
                                    ))}
                                </Box>
                            </Drawer>
                        </>
                    ) : (
                        <Box sx={{ flexGrow: 1, display: 'flex', justifyContent: 'flex-end' }}>
                            {navItems.map((item) => (
                                <Button
                                    key={item.label}
                                    sx={{ color: '#fff' }}
                                    onClick={() => navigate(item.path)}
                                >
                                    {item.label}
                                </Button>
                            ))}
                        </Box>
                    )}
                    <Box>
                        <SwitchLanguage />
                    </Box>
                </Toolbar>
            </AppBar>

            <Loader />
            <div style={{ paddingTop: appBarHeight }}>
                {children}
            </div>
        </div>
    );
};

export default Layout;
