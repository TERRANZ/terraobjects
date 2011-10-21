package ru.terraobjects.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.terraobjects.solutions.Solution;
import ru.terraobjects.solutions.annotation.ASolution;
import ru.terraobjects.solutions.impl.hashscan.HashScanSolution;
import ru.terraobjects.solutions.impl.terrastore.TerraStoreSolution;

/**
 *
 * @author korostelev
 */
public class SolutionManager
{

    private static SolutionManager instance = new SolutionManager();
    private static final String SOLUTIONS_CLASS_PATH = "ru.terraobjects.solutions.impl";
    private ArrayList<Solution> solutions = new ArrayList<Solution>();

    private String scanLib()
    {
        final Collection<File> all = new LinkedList<File>();
        addFilesRecursively(new File("."), all);
        String ret = "";
        for (File f : all)
        {
            try
            {
                if (!f.isDirectory())
                {
                    ret += f.getAbsolutePath() + ";";
                }
            } catch (Exception ex)
            {
                Logger.getLogger(SolutionManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ret.substring(0, ret.length() - 1);
    }

    private static void addFilesRecursively(File file, Collection<File> all)
    {
        final File[] children = file.listFiles();
        if (children != null)
        {
            for (File child : children)
            {
                all.add(child);
                addFilesRecursively(child, all);
            }
        }
    }

    private SolutionManager()
    {
        try
        {
            String cp = System.getProperty("java.class.path");
            System.out.println("CP " + cp);
            List<String> classesScanned = new LinkedList<String>();
            classesScanned = scan(cp);
            for (String cn : classesScanned)
            {
                cn = cn.replace("/", ".");
                System.out.println("Founded class " + cn);

                if (cn.contains(SOLUTIONS_CLASS_PATH))
                {
                    System.out.println("Founded class " + cn);

                    //Ок, наш клиент
                    System.out.println("Ok, found " + cn + " loading it");
                    try
                    {
                        Solution newClass = (Solution) Class.forName(cn).newInstance();
                        ASolution annotation = (ASolution) newClass.getClass().getAnnotation(ASolution.class);
                        if (annotation != null)
                        {
                            //Ок, совсем хорошо, нашлась нужная аннотация
                            solutions.add(newClass);
                            System.out.println("Ok, found " + cn + " it's out client!");
                        }
                    } catch (Exception e)
                    {
                        // System.out.println("Can't load class " + cn + " it's not a solution");
                    }

                }
            }
        } catch (Exception ex)
        {
            Logger.getLogger(SolutionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static SolutionManager getInstance()
    {
        return instance;
    }

    public List<Solution> getSolutions()
    {
        //return solutions;
        ArrayList<Solution> sols = new ArrayList<Solution>();
        sols.add(new HashScanSolution());
        sols.add(new TerraStoreSolution());
        return sols;
    }

    /**
     * Просканировать строку, представляющую из себя classpath и вернуть
     * коллекцию найденных классов
     * 
     * @param cp
     *            classpath
     * @return коллекция обнаруженных классов
     * @throws IOException
     */
    public static ArrayList<String> scan(String cp) throws IOException
    {
        ArrayList<String> classes = new ArrayList<String>();
        scan(cp, classes);
        return classes;
    }

    /**
     * Просканировать строку, представляющую из себя classpath и добавить все
     * найденные классы в коллекцию
     * 
     * @param cp
     *            classpath
     * @param classes
     *            коллекция классов, в которую добавляются результаты
     * @throws IOException
     */
    public static void scan(String cp, Collection<String> classes) throws IOException
    {
        String[] entries = cp.split(File.pathSeparator);

        for (String entryName : entries)
        {
            File file = new File(entryName);
            if (file.isDirectory())
            {
                scanDir("", file, classes);
            } else if (file.getName().endsWith(".jar") || file.getName().endsWith(".zip"))
            {
                scanJar(file, classes);
            } else
            {
                //throw new IOException("Unknown classpath entry " + file.getName());
            }
        }
    }

    /**
     * Сканировать Jar-файл на предмет наличия class-файлов
     * 
     * @param jarFile
     *            файл архива
     * @param classes
     *            коллекция классов, в которую добавляются результаты
     * @throws IOException
     */
    private static void scanJar(File jarFile, Collection<String> classes) throws IOException
    {
        JarFile jar = new JarFile(jarFile); // Открываем Jar-файл для чтения списка файлов

        for (Enumeration<JarEntry> e = jar.entries(); e.hasMoreElements();)
        {
            JarEntry entry = e.nextElement();

            if (entry.isDirectory()) // Попускаем директории
            {
                continue;
            }

            scanFileName(entry.getName(), classes); // Если элемент - файл, проверяем его имя
        }

        jar.close();
    }

    /**
     * Сканировать директорию, представляющую пакет на предмет наличия
     * class-файлов.
     * 
     * @param pkg
     *            имя пакета
     * @param dir
     *            директория
     * @param classes
     *            коллекция классов, в которую добавляются результаты
     */
    private static void scanDir(String pkg, File dir, Collection<String> classes)
    {
        for (File file : dir.listFiles())
        { // Перебираем все файлы в директории
            if (file.isDirectory())
            {
                scanDir(pkg + file.getName() + File.separator, file, classes); // Сканируем директорию пакета
            } else
            {
                scanFileName(pkg + file.getName(), classes); // Проверяем имя файла
            }
        }
    }

    /**
     * Проверить имя файла и извлечь имя класса
     * 
     * @param name
     *            имя файла
     * @param classes
     *            коллекция классов, в которую добавляются результаты
     */
    private static void scanFileName(String name, Collection<String> classes)
    {
        if (!name.endsWith(".class"))
        {
            return;
        }

        classes.add(name.substring(0, name.length() - 6).replace(File.separator, ".")); // Извлекаем имя класса из имени файла: удаляем расширение и преобразуем разделители
    }
}
