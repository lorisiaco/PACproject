\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{softwaredoc}[2025/01/15 Modern software documentation style]

% Base class
\LoadClass[a4paper,12pt]{article}

% Required packages
\RequirePackage[utf8]{inputenc}
\RequirePackage[T1]{fontenc}
\RequirePackage{lmodern}
\RequirePackage{geometry}
\RequirePackage{titlesec}
\RequirePackage{fancyhdr}
\RequirePackage{hyperref}
\RequirePackage{xcolor}
\RequirePackage{listings}
\RequirePackage{graphicx}
\RequirePackage{amsmath,amssymb}
\RequirePackage{tocloft}
\RequirePackage{parskip}

% Page layout
\geometry{margin=1in}
\setlength{\parindent}{0pt}

% Color scheme
\definecolor{primary}{HTML}{003366} % Dark blue
\definecolor{secondary}{HTML}{007ACC} % Light blue
\definecolor{background}{HTML}{F5F5F5} % Light grey
\definecolor{codegreen}{HTML}{228B22} % Green for code comments
\definecolor{codeblue}{HTML}{0000FF} % Blue for keywords
\definecolor{codegray}{HTML}{808080} % Grey for code
\definecolor{codepurple}{HTML}{800080} % Purple for strings

% Title format
\titleformat{\section}{\sffamily\Large\bfseries\color{primary}}{\thesection}{1em}{}
\titleformat{\subsection}{\sffamily\large\bfseries\color{secondary}}{\thesubsection}{1em}{}
\titleformat{\subsubsection}{\sffamily\normalsize\bfseries\color{primary}}{\thesubsubsection}{1em}{}

% Header and footer
\fancyhf{}
\fancyhead[L]{\textsf{Software Documentation}}
\fancyhead[R]{\textsf{\today}}
\fancyfoot[C]{\thepage}
\pagestyle{fancy}

% Code style
\lstdefinestyle{custom}{
    backgroundcolor=\color{background},
    commentstyle=\color{codegreen},
    keywordstyle=\color{codeblue},
    stringstyle=\color{codepurple},
    basicstyle=\ttfamily\small,
    breaklines=true,
    numbers=left,
    numberstyle=\tiny\color{codegray},
    frame=single,
    tabsize=4
}

% TOC customization
\renewcommand{\cftsecfont}{\sffamily\bfseries\color{primary}}
\renewcommand{\cftsubsecfont}{\sffamily\color{secondary}}
\renewcommand{\cftsecleader}{\cftdotfill{\cftdotsep}}

% Custom commands
\newcommand{\code}[1]{\texttt{#1}}
\newcommand{\important}[1]{\textbf{\textcolor{secondary}{#1}}}

\endinput