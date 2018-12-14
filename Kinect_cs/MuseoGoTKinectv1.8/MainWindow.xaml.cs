
namespace Microsoft.Samples.Kinect.GOT
{
    using System;
    using System.IO;
    using System.Windows;
    using System.Windows.Data;
    using System.Windows.Media;
    using System.Windows.Media.Imaging;
    using Microsoft.Kinect;
    using Microsoft.Kinect.Toolkit;
    using Microsoft.Kinect.Toolkit.Controls;
    using Microsoft.Samples.Kinect.ControlsBasics;


    /// <summary>
    /// Interaction logic for MainWindow
    /// </summary>
    public partial class MainWindow
    {

        /// <summary>
        /// Active Kinect sensor
        /// </summary>
        private KinectSensor sensor;
        private Detector mov;
        private int actualFaction = -1;
        private String actualWindow = "MainWindow";
        private String targaryenInfo = "La Casa Targaryen es una casa noble de ascendencia Valyria que escapó de la Maldición. Sus asentamientos eran la capital del reino Desembarco del Rey," 
            + "la isla de Rocadragón y el castillo de Refugio Estival. Su emblema es un dragón de tres cabezas de gules en campo sable, representando a Aegon I y sus hermanas"+
            "Rhaenys y Visenya.\nSu lema es Fuego y Sangre.Sus miembros vivieron durante siglos en la isla de Rocadragón hasta que Aegon Targaryen y sus hermanas" +
            "montaron sus dragones y conquistaron seis de los Siete Reinos. Gobernaron Poniente durante casi 300 años hasta la Rebelión de Robert."
            + "Sus espadas ancestrales de acero valyrio, ambas en actual paradero desconocido, son Fuegoscuro y Hermana Oscura.";
        private String dothrakiInfo = "Los Dothraki son una cultura de guerreros nómadas de Essos, con una historia conocida que se remonta a hace unos cuatrocientos años, " +
            "poco antes de que Aegon I desembarcase en Poniente. \nViven en los vastos pastizales del Mar Dothraki, en hordas conocidas como khalasares.Los dothraki suelen ser corpulentos, " +
            "de piel cobriza y oscura, ojos almendrados y cabello comúnmente de color negro.";
        private String facelessInfo = "El grupo se originó en las minas volcánicas de esclavos de Valyria. Su fundador se dio cuenta que todos los esclavos de Valyria, a pesar de sus distintos orígenes," +
            " oraban al mismo dios de la muerte, sólo que cada encarnación era una faceta del mismo.\nLa sociedad cree que todos estos dioses no son más que facetas de un Dios, el Dios de Muchos Rostros. " +
            "\nDe acuerdo a la sociedad, el dios está presente en muchas religiones, sólo que adopta diferentes nombres.\nEn Qohor es llamado la Cabra Negra, en Yi Ti, el León de Noche y en la Fe de los Siete, " +
            "el Desconocido.\nEn el templo de la sociedad, la Casa de Blanco y Negro, los seguidores visten túnicas blancas y negras y realizan tareas a la comunidad, tales como atender a los muertos." +
            "\nLa Casa contiene una fuente y altares con muchos ídolos, representando los distintos dioses de la muerte, incluido el Desconocido de la Fe de los Siete, pero no existen servicios religiosos formales. \n" +
            "Algunos visitantes oran y encienden velas al dios, luego beben del agua de la fuente usando una copa negra." +
            "\nLos religiosos llenan la fuente con veneno, por lo que aquellos que beben de ella sufren una muerte sin dolor.Esto es llamado el regalo del Dios de Muchos Rostros."
            +"Una frase asociada al culto del Dios de Muchos Rostros es Valar Morghulis, que traducido del Alto Valyrio significa todos los hombres deben morir; la respuesta a esta frase es Valar Dohaeris, " +
            "que significa todos los hombres deben servir.";
        private String starkInfo = "La Casa Stark de Invernalia es una casa noble del Norte.\nSu asentamiento es Invernalia.Durante siglos,"
            +"fue la casa principal del Norte y su linaje se extiende hasta los Primeros Hombres, gobernando el Norte como reyes por derecho propio."+
            "\nSu emblema es un lobo huargo de cenizo corriendo sobre campo de plata. Su lema es Se acerca el Invierno. Su espada ancestral de acero valyrio se llamaba Hielo.";
        private String baratheonInfo = "La Casa Baratheon de Bastión de Tormentas es una casa noble de las Tierras de la Tormenta.\n" +
            "Su asentamiento es Bastión de Tormentas, que fue residencia de la Casa Durrandon, los antiguos Reyes de la Tormenta.\n" +
            "Su emblema es un venado coronado de sable sobre campo de oro. Su lema es Nuestra es la Furia.";
        private String lannisterInfo = "La Casa Lannister de Roca Casterly es la principal casa noble de las Tierras del Oeste. "+ "\nSu asentamiento es Roca Casterly." + 
            "\nSu emblema es un león rampante de oro sobre campo de gules.\nSu lema es ¡Oye mi Rugido!, aunque su lema no oficial," + 
            " Un Lannister siempre paga sus deudas, es más conocido.";

        private readonly KinectSensorChooser sensorChooser;

        /// <summary>
        /// Initializes a new instance of the <see cref="MainWindow"/> class. 
        /// </summary>
        public MainWindow()
        {
            this.InitializeComponent();
            mov = new Detector();

            // initialize the sensor chooser and UI
            this.sensorChooser = new KinectSensorChooser();
            this.sensorChooser.KinectChanged += SensorChooserOnKinectChanged;
            this.sensorChooserUi.KinectSensorChooser = this.sensorChooser;
            this.sensorChooser.Start();

            // Bind the sensor chooser's current sensor to the KinectRegion
            var regionSensorBinding = new Binding("Kinect") { Source = this.sensorChooser };
            BindingOperations.SetBinding(this.map_buttons, KinectRegion.KinectSensorProperty, regionSensorBinding);
            BindingOperations.SetBinding(this.essos_buttons, KinectRegion.KinectSensorProperty, regionSensorBinding);
            BindingOperations.SetBinding(this.westeros_buttons, KinectRegion.KinectSensorProperty, regionSensorBinding);
            BindingOperations.SetBinding(this.info_region, KinectRegion.KinectSensorProperty, regionSensorBinding);

        }

        /// <summary>
        /// Execute startup tasks
        /// </summary>
        /// <param name="sender">object sending the event</param>
        /// <param name="e">event arguments</param>
        private void WindowLoaded(object sender, RoutedEventArgs e)
        {

            // Look through all sensors and start the first connected one.
            // This requires that a Kinect is connected at the time of app startup.
            // To make your app robust against plug/unplug, 
            // it is recommended to use KinectSensorChooser provided in Microsoft.Kinect.Toolkit (See components in Toolkit Browser).
            foreach (var potentialSensor in KinectSensor.KinectSensors)
            {
                if (potentialSensor.Status == KinectStatus.Connected)
                {
                    this.sensor = potentialSensor;
                    break;
                }
            }

            if (null != this.sensor)
            {
                // Turn on the skeleton stream to receive skeleton frames
                this.sensor.SkeletonStream.Enable();

                // Add an event handler to be called whenever there is new color frame data
                this.sensor.SkeletonFrameReady += this.SensorSkeletonFrameReady;

                this.sensor.SkeletonStream.TrackingMode = SkeletonTrackingMode.Default;
                // Start the sensor!
                try
                {
                    this.sensor.Start();
                }
                catch (IOException)
                {
                    this.sensor = null;
                }
            }
        }

        /// <summary>
        /// Execute shutdown tasks
        /// </summary>
        /// <param name="sender">object sending the event</param>
        /// <param name="e">event arguments</param>
        private void WindowClosing(object sender, System.ComponentModel.CancelEventArgs e)
        {
            if (null != this.sensor)
            {
                this.sensor.Stop();
            }
        }
        
        
        /// <summary>
        /// Event handler for Kinect sensor's SkeletonFrameReady event
        /// </summary>
        /// <param name="sender">object sending the event</param>
        /// <param name="e">event arguments</param>
        /// Aqui va los gestos
        /// 
        private void SensorSkeletonFrameReady(object sender, SkeletonFrameReadyEventArgs e)
        {
            Skeleton[] skeletons = new Skeleton[0];

            using (SkeletonFrame skeletonFrame = e.OpenSkeletonFrame())
            {
                if (skeletonFrame != null)
                {
                    skeletons = new Skeleton[skeletonFrame.SkeletonArrayLength];
                    skeletonFrame.CopySkeletonDataTo(skeletons);
                }
            }
            if (skeletons.Length != 0)
            {
                foreach (Skeleton skel in skeletons)
                {
                    if (skel.TrackingState == SkeletonTrackingState.Tracked)
                    {
  
                        if (mov.deteccion(skel))
                        {
                            //westeros_button.Opacity = (westeros_button.Opacity + 1) % 2;
                            if (actualWindow != "MainWindow")
                            {
                                goMainWindow();
                            }
                        }
                        if (mov.deteccion2(skel))
                        {
                            westeros_button.Opacity = (westeros_button.Opacity + 1) % 2;
                        }
                        if (mov.deteccion3(skel))
                        {
                               essos_button.Opacity = (essos_button.Opacity + 1) % 2;
                        }
                        break;
                    }

                }
            }


        }

        /// <summary>
        /// Called when the KinectSensorChooser gets a new sensor
        /// </summary>
        /// <param name="sender">sender of the event</param>
        /// <param name="args">event arguments</param>
        private static void SensorChooserOnKinectChanged(object sender, KinectChangedEventArgs args)
        {
            if (args.OldSensor != null)
            {
                try
                {
                    args.OldSensor.DepthStream.Range = DepthRange.Default;
                    args.OldSensor.SkeletonStream.EnableTrackingInNearRange = false;
                    args.OldSensor.DepthStream.Disable();
                    args.OldSensor.SkeletonStream.Disable();
                }
                catch (InvalidOperationException)
                {
                    // KinectSensor might enter an invalid state while enabling/disabling streams or stream features.
                    // E.g.: sensor might be abruptly unplugged.
                }
            }

            if (args.NewSensor != null)
            {
                try
                {
                    args.NewSensor.DepthStream.Enable(DepthImageFormat.Resolution640x480Fps30);
                    args.NewSensor.SkeletonStream.Enable();

                    try
                    {
                        //args.NewSensor.DepthStream.Range = DepthRange.Near;
                        args.NewSensor.SkeletonStream.EnableTrackingInNearRange = true;
                    }
                    catch (InvalidOperationException)
                    {
                        // Non Kinect for Windows devices do not support Near mode, so reset back to default mode.
                        args.NewSensor.DepthStream.Range = DepthRange.Default;
                        args.NewSensor.SkeletonStream.EnableTrackingInNearRange = false;
                    }
                }
                catch (InvalidOperationException)
                {
                    // KinectSensor might enter an invalid state while enabling/disabling streams or stream features.
                    // E.g.: sensor might be abruptly unplugged.
                }
            }
        }
        
        private void changeFaction(int actual)
        {


        }
        private void goMainWindow()
        {
            westeros_buttons.Visibility = Visibility.Hidden;
            essos_buttons.Visibility = Visibility.Hidden;
            map_buttons.Visibility = Visibility.Visible;
            info_region.Visibility = Visibility.Hidden;
            var fondo = new ImageBrush();
            fondo.ImageSource = new BitmapImage(new Uri("../Images/mapa.jpg", UriKind.Relative));
            main_grid.Background = fondo;

        }

        private void goEssos() {
            essos_buttons.Visibility = Visibility.Visible;
            map_buttons.Visibility = Visibility.Hidden;
            var essos = new ImageBrush();
            essos.ImageSource = new BitmapImage(new Uri("../Images/essos.jpg", UriKind.Relative));
            main_grid.Background = essos;
        }

        private void goWesteros()
        {
            westeros_buttons.Visibility = Visibility.Visible;
            map_buttons.Visibility = Visibility.Hidden;
            var westeros = new ImageBrush();
            westeros.ImageSource = new BitmapImage(new Uri("../Images/westeros.jpg", UriKind.Relative));
            main_grid.Background = westeros;
        }

        private void showInfo(String factionName)
        {
            var background = new ImageBrush();
            background.ImageSource = new BitmapImage(new Uri("../Images/got.jpg", UriKind.Relative));
            main_grid.Background = background;
            String data;

            switch (actualWindow)
            {
                case "EssosInfo":
                    essos_buttons.Visibility = Visibility.Hidden;
                    break;

                case "WesterosInfo":
                    westeros_buttons.Visibility = Visibility.Hidden;
                    break;
            }
            switch (factionName)
            {
                case "targaryen":
                    data = targaryenInfo;
                    actualFaction = 0;
                    break;

                case "dothraki":
                    actualFaction = 1;
                    data = dothrakiInfo;

                    break;
                case "faceless":
                    actualFaction = 2;
                    data = facelessInfo;

                    break;
                case "stark":
                    actualFaction = 0;
                    data = starkInfo;

                    break;
                case "lannister":
                    actualFaction = 1;
                    data = lannisterInfo;

                    break;
                case "baratheon":
                    actualFaction = 2;
                    data = baratheonInfo;
                    break;

                default:
                    data = "";
                    break;

            }

            info_region.Visibility = Visibility.Visible;
            info_textb.Text = data;
        }
        /// <summary>
        /// Handle a button click from the wrap panel.
        /// </summary>
        /// <param name="sender">Event sender</param>
        /// <param name="e">Event arguments</param>
        private void KinectTileButtonClick(object sender, RoutedEventArgs e)
        {
            String button_name = (sender as KinectTileButton).Name.ToString();
            switch (button_name)
            {
                case "essos_button":
                    goEssos();
                    actualWindow = "EssosWindow";
                    break;

                case "westeros_button":
                    goWesteros();
                    actualWindow = "WesterosWindow";

                    break;

                case "targaryen_button":
                    actualWindow = "EssosInfo";
                    showInfo("targaryen");

                    break;

                case "dothraki_button":
                    actualWindow = "EssosInfo";
                    showInfo("dothraki");


                    break;

                case "faceless_button":
                    actualWindow = "EssosInfo";
                    showInfo("faceless");

                    break;

                case "stark_button":
                    actualWindow = "WesterosInfo";
                    showInfo("stark");

                    break;

                case "lannister_button":
                    actualWindow = "WesterosInfo";
                    showInfo("lannister");

                    break;

                case "baratheon_button":
                    actualWindow = "WesterosInfo";
                    showInfo("baratheon");

                    break;   
            }
        }
    }
}
