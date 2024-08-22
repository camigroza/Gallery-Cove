import { useEffect, useState } from 'react';
import { LatLngExpression } from 'leaflet';
import { MapContainer, Marker, Popup, TileLayer } from 'react-leaflet';

interface Props {
    locationData: any;
    index: number;
}

export default function MapLeaflet({ locationData, index }: Props) {
    const [latLon, setLatLong] = useState<LatLngExpression>([0, 0]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        setTimeout(() => {
            fetchLocation();
        }, index * 1300)
    }, []);
    
    async function fetchLocation() {
        const { city, street, number } = locationData;
        // https://locationiq.com/
        //const key = 'pk.c24c8314ae6431abc37cfa625f1f486e';
        const key = 'pk.ea95a9dcafb273a541b3a74760da644d';
        const url = `https://us1.locationiq.com/v1/search?key=${key}&q=${number} ${street} ${city}&format=json&`
        try {
            const response = await fetch(url)
            const data = await response.json();
            if (data.length > 0) {
                const { lat, lon } = data[0];
                setLatLong([Number(lat), Number(lon)]);
                setLoading(false)
            }
        } catch (error) {
            console.error('Error geocoding address:', error);
        }
    }

    return (
        <>
            {loading ? (
                <div style={{
                    position: 'absolute',
                    left: "50%",
                    top: "50%",
                    transform: "translate(-50%, -50%)"
                }}>Loading Map...</div>
            ) : (
                <MapContainer center={latLon} zoom={13} scrollWheelZoom={false} style={{height: "100%", width: "100%", zIndex: 0}}>
                    <TileLayer
                     
                        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                    />
                    <Marker position={latLon}>
                        <Popup>
                           {Object.values(locationData).join(", ")}
                        </Popup>
                    </Marker>
                </MapContainer>
            )}
        </>
    )
};