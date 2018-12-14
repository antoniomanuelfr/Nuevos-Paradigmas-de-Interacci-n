using Microsoft.Kinect;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Microsoft.Samples.Kinect.ControlsBasics
{
    class Movement2
    {


        private int fase;
        private float factorA, factorB,factorC;
        private int cont; 
        public Movement2(float a = 1.3f, float b = 0.7f , float c = 1.4f)
        {
            fase = 0;
            factorA = a;
            factorB = b;
            factorC = c;
        }

        public int getFase()
        {
            return fase;
        }
        public void setFase(int a)
        {
            fase=a;
        }

        private bool Reposo(Skeleton esqueleto)
        {
            Joint[] puntos = new Joint[3];
            //Tomamos los 3 puntos de referencia para comprobar el estado de reposo
            puntos[0] = esqueleto.Joints[JointType.ShoulderRight];
            puntos[1] = esqueleto.Joints[JointType.ElbowRight];
            puntos[2] = esqueleto.Joints[JointType.WristRight];
            //if((puntos[2].Position.Y > (puntos[1].Position.Y * 0.5)) && (puntos[2].Position.Y < (puntos[1].Position.Y * 1.5))) //altura de muñeca y hombro igual
            if(puntos[2].Position.X >(puntos[1].Position.X)*1.5)
            {
                return true;
            }
            else
            {
                return false;
            }


        }
        public bool movimiento(Skeleton esqueleto)
        {
            Joint[] puntos = new Joint[3];
            //Tomamos los 3 puntos de referencia para comprobar el movimiento
            puntos[0] = esqueleto.Joints[JointType.ShoulderRight];
            puntos[1] = esqueleto.Joints[JointType.ElbowRight];
            puntos[2] = esqueleto.Joints[JointType.WristRight];
            if( fase ==0) {
                if (Reposo(esqueleto))
                {
                    fase = 1;
                    //Console.WriteLine("111111111");
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if (fase == 1) {
                cont++;
                if (puntos[2].Position.X < (puntos[1].Position.X)) //mientras que la mano este al altura del hombro
                {
                    fase = 2;
                    cont = 0;
                    return true;
                    
                }
                else
                {
                    cont ++;
                    Console.WriteLine(cont.ToString());
                    if (cont == 40)
                    {
                        cont = 0;
                        fase = 0;
                        return false;
                    }
                    return true;
                        
                }
            }
            else
            {
                return true;
            }
        }
    }
}
